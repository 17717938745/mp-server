<template>
  <div class="login-wrap" :style="{
    backgroundImage: `url(${fullUrl('/third/img/login-bg.jpg', '')})`
  }">
    <div class="ms-login">
      <div class="ms-title">管理平台</div>
      <el-form
          :model="signData"
          :rules="rules"
          ref="login"
          label-width="0px"
          class="ms-content"
      >
        <el-form-item prop="username">
          <el-input v-model="signData.username" tabindex="1" autofocus placeholder="username">
            <!--suppress HtmlUnknownAttribute -->
            <template #prepend>
              <el-button :icon="User"></el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
              type="password"
              placeholder="password"
              v-model="signData.password"
              @keyup.enter="handleSubmit"
              tabindex="1"
          >
            <!--suppress HtmlUnknownAttribute -->
            <template #prepend>
              <el-button :icon="Unlock"></el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-button class="login-btn" type="primary" @click="handleSubmit" :disabled="loginState">登录</el-button>
        <span class="login-tips">Tips：欢迎使用后台管理系统</span>
        <el-link class="login-helper" @click="handleToWhatYouWant">
          已登录直接跳转
        </el-link>
      </el-form>
    </div>
  </div>
</template>

<script lang="tsx" setup>
import {reactive, ref, Ref, toRefs} from "vue";
import {useRouter} from "vue-router";
import {Store, useStore} from "vuex";
import {ElMessage} from "element-plus";
import {getSignData, setDeviceId, setSalt, setSignData, setUsername,} from "../../util/StorageUtil";
import {refreshRouter} from "../../router/Industry"
import {Unlock, User} from "@element-plus/icons-vue"
import {DataResult, ListResult, MenuTree, SignParam} from "../../typing/ma/System"
import {StoreType} from "../../store/Industry"
import {getParam, shownMenuTreeList, getFirstPath} from '../../util/RouterUtil'
import {httpGet, httpPutJson, initClient} from '@/util/HttpUtil'
import {sm4Encrypt} from '@/util/SecurityUtil'
import {fullUrl} from '@/util/EnvUtil'

const router = useRouter();
const login: Ref = ref(null);
const store: Store<StoreType> = useStore();
const storeState: StoreType = store.state;
store.commit("clearTagList");
const defaultParam: SignParam =
    Object.keys(getSignData()).length > 0
        ? getSignData()
        : {
          username: "",
          password: "",
          autoLogin: true,
        };
const state = reactive({
  signData: defaultParam,
  rules: {
    username: [
      {
        required: true,
        message: "请输入用户名",
        trigger: "blur",
      },
    ],
    password: [{required: true, message: "请输入密码", trigger: "blur"}],
  },
});

const loginState = ref(false)
const {signData, rules} = {...toRefs(state)};
const handleToWhatYouWant = () => {
  const menuTreeList = shownMenuTreeList(storeState.menuTreeList)
  const goBack = getParam("goBack") || ''
  if (goBack.length > 0 && router.hasRoute(goBack)) {
    router.push(goBack)
  } else {
    const firstPath = getFirstPath(menuTreeList, '/industry')
    if (!router.hasRoute(firstPath)) {
      console.log(`firstPath: ${firstPath}`)
      loginState.value = false
    }
    router.push(firstPath)
  }
}
const handleSubmit = (retry: boolean = true) => {
  login.value.validate((valid: any) => {
    if (valid && !loginState.value) {
      loginState.value = true
      httpPutJson("system/sign-in", {
        username: state.signData.username,
        passwordEncrypt: sm4Encrypt(state.signData.password || ''),
      })
      .then((result: DataResult<any>) => {
        const user = result.data.user
        const allMenuTreeList = [
          {
            "children": [],
            "id": "-1",
            "path": "dashboard",
            "component": "dashboard",
            "nameKey": "dashboard",
            "white": false,
            "showFlag": true,
          },
          {
            "id": "-1",
            "path": "system",
            "component": "",
            "nameKey": "systemManage",
            "white": false,
            "showFlag": true,
            "children": [
              {
                "children": [],
                "id": "-1",
                "path": "user",
                "component": "user",
                "nameKey": "userManage",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [],
                "id": "-1",
                "path": "scheduling",
                "component": "scheduling",
                "nameKey": "schedulingCnc",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [],
                "id": "-1",
                "path": "vocation",
                "component": "vocation",
                "nameKey": "vocationRecord",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [],
                "id": "-1",
                "path": "visitor",
                "component": "visitor",
                "nameKey": "visitorManage",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [],
                "id": "-1",
                "path": "account",
                "component": "account",
                "nameKey": "accountManage",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [],
                "id": "-1",
                "path": "sign",
                "component": "sign",
                "nameKey": "signInHistory",
                "white": false,
                "showFlag": user.username === 'admin',
              },
            ],
          },
          {
            "id": "-1",
            "path": "product",
            "component": "",
            "nameKey": "productManage",
            "white": false,
            "showFlag": true,
            "children": [
              {
                "children": [],
                "id": "-1",
                "path": "param",
                "component": "param",
                "nameKey": "paramManage",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [],
                "id": "-1",
                "path": "device",
                "component": "device",
                "nameKey": "deviceManage",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [],
                "id": "-1",
                "path": "order",
                "component": "order",
                "nameKey": "orderManage",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [],
                "id": "-1",
                "path": "quotation",
                "component": "quotation",
                "nameKey": "quotationManage",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [],
                "id": "-1",
                "path": "index",
                "component": "index",
                "nameKey": "productQuotaManage",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [],
                "id": "-1",
                "path": "report",
                "component": "report",
                "nameKey": "userRunReport",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [],
                "id": "-1",
                "path": "examine",
                "component": "examine",
                "nameKey": "orderInspectionRecord",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [],
                "id": "-1",
                "path": "assembly",
                "component": "assembly",
                "nameKey": "completeMachineAssemblyRecord",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [],
                "id": "-1",
                "path": "box",
                "component": "box",
                "nameKey": "boxFlagCard",
                "white": false,
                "showFlag": true,
              },
            ],
          },
          {
            "id": "-1",
            "path": "report",
            "component": "",
            "nameKey": "reportManage",
            "white": false,
            "showFlag": true,
            "children": [
              {
                "children": [
                  {
                    "children": [],
                    "id": "-1",
                    "path": "event",
                    "component": "event",
                    "nameKey": "accidentEvent",
                    "white": false,
                    "showFlag": true,
                  },
                  {
                    "children": [],
                    "id": "-1",
                    "path": "improve",
                    "component": "improve",
                    "nameKey": "accidentImprove",
                    "white": false,
                    "showFlag": true,
                  },
                  {
                    "children": [],
                    "id": "-1",
                    "path": "quality",
                    "component": "quality",
                    "nameKey": "accidentQuality",
                    "white": false,
                    "showFlag": true,
                  },
                  {
                    "children": [],
                    "id": "-1",
                    "path": "crash",
                    "component": "crash",
                    "nameKey": "accidentCrash",
                    "white": false,
                    "showFlag": true,
                  },
                  {
                    "children": [],
                    "id": "-1",
                    "path": "trouble",
                    "component": "trouble",
                    "nameKey": "accidentTrouble",
                    "white": false,
                    "showFlag": true,
                  },
                ],
                "id": "-1",
                "path": "accident",
                "component": "",
                "nameKey": "accidentReport",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [
                  {
                    "children": [],
                    "id": "-1",
                    "path": "process-group-check",
                    "component": "processGroupCheck",
                    "nameKey": "processGroupCheck",
                    "white": false,
                    "showFlag": true,
                  },
                  {
                    "children": [],
                    "id": "-1",
                    "path": "ehs-safety-group-check",
                    "component": "ehsSafetyGroupCheck",
                    "nameKey": "ehsSafetyGroupCheck",
                    "white": false,
                    "showFlag": true,
                  },
                  {
                    "children": [],
                    "id": "-1",
                    "path": "quality-department-check",
                    "component": "qualityDepartmentCheck",
                    "nameKey": "qualityDepartmentCheck",
                    "white": false,
                    "showFlag": true,
                  },
                ],
                "id": "-1",
                "path": "inspection",
                "component": "inspectionReport",
                "nameKey": "inspectionReport",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [],
                "id": "-1",
                "path": "disqualification",
                "component": "disqualification",
                "nameKey": "disqualificationOrder",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [],
                "id": "-1",
                "path": "plan",
                "component": "plan",
                "nameKey": "keepImprove",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [],
                "id": "-1",
                "path": "score",
                "component": "score",
                "nameKey": "scoreReport",
                "white": false,
                "showFlag": true,
              },
            ]
          },
          {
            "id": "-1",
            "path": "design",
            "component": "",
            "nameKey": "designManage",
            "white": false,
            "showFlag": true,
            "children": [
              {
                "children": [],
                "id": "-1",
                "path": "material",
                "component": "material",
                "nameKey": "material",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [],
                "id": "-1",
                "path": "task",
                "component": "task",
                "nameKey": "taskManage",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [],
                "id": "-1",
                "path": "inventory",
                "component": "inventory",
                "nameKey": "inventoryOutOfPlan",
                "white": false,
                "showFlag": true,
              },
            ]
          },
          {
            "id": "-1",
            "path": "asset",
            "component": "",
            "nameKey": "assetManage",
            "white": false,
            "showFlag": true,
            "children": [
              {
                "children": [],
                "id": "-1",
                "path": "device",
                "component": "device",
                "nameKey": "deviceCheckLedger",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [],
                "id": "-1",
                "path": "dress",
                "component": "dress",
                "nameKey": "workDressManage",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [],
                "id": "-1",
                "path": "computer",
                "component": "computer",
                "nameKey": "computerManage",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [],
                "id": "-1",
                "path": "template",
                "component": "template",
                "nameKey": "templateManage",
                "white": false,
                "showFlag": true,
              },
              {
                "children": [
                  {
                    "children": [],
                    "id": "-1",
                    "path": "equipment",
                    "component": "equipment",
                    "nameKey": "machineEquipment",
                    "white": false,
                    "showFlag": true,
                  },
                  {
                    "children": [],
                    "id": "-1",
                    "path": "maintain",
                    "component": "maintain",
                    "nameKey": "machineMaintainRepair",
                    "white": false,
                    "showFlag": true,
                  },
                ],
                "id": "-1",
                "path": "machine",
                "nameKey": "productMachine",
                "white": false,
                "showFlag": true,
              },
            ]
          },
        ]
        const menuTreeList: MenuTree[] = [
          {
            "id": "-1",
            "path": "industry",
            "component": "industry",
            "white": true,
            "showFlag": true,
            "children": [
              {
                "id": "-1",
                "path": "admin",
                "component": "admin",
                "nameKey": "admin",
                "white": true,
                "showFlag": true,
                "children": '3' === user.userProperty ? [
                  {
                    "id": "-1",
                    "path": "system",
                    "component": "",
                    "nameKey": "systemManage",
                    "white": false,
                    "showFlag": true,
                    "children": [
                      {
                        "children": [],
                        "id": "-1",
                        "path": "visitor",
                        "component": "visitor",
                        "nameKey": "visitorManage",
                        "white": false,
                        "showFlag": true,
                      },
                    ],
                  },
                  {
                    "id": "-1",
                    "path": "report",
                    "component": "",
                    "nameKey": "reportManage",
                    "white": false,
                    "showFlag": true,
                    "children": [
                      {
                        "children": [
                          {
                            "children": [],
                            "id": "-1",
                            "path": "quality",
                            "component": "quality",
                            "nameKey": "accidentQuality",
                            "white": false,
                            "showFlag": true,
                          },
                        ],
                        "id": "-1",
                        "path": "accident",
                        "component": "",
                        "nameKey": "accidentReport",
                        "white": false,
                        "showFlag": true,
                      },
                      {
                        "children": [],
                        "id": "-1",
                        "path": "disqualification",
                        "component": "disqualification",
                        "nameKey": "disqualificationOrder",
                        "white": false,
                        "showFlag": true,
                      },
                    ]
                  },
                  {
                    "id": "-1",
                    "path": "design",
                    "component": "",
                    "nameKey": "designManage",
                    "white": false,
                    "showFlag": true,
                    "children": [
                      {
                        "children": [],
                        "id": "-1",
                        "path": "task",
                        "component": "task",
                        "nameKey": "taskManage",
                        "white": false,
                        "showFlag": true,
                      },
                    ]
                  }, {
                    "id": "-1",
                    "path": "asset",
                    "component": "",
                    "nameKey": "assetManage",
                    "white": false,
                    "showFlag": true,
                    "children": [
                      {
                        "children": [],
                        "id": "-1",
                        "path": "template",
                        "component": "template",
                        "nameKey": "templateManage",
                        "white": false,
                        "showFlag": true,
                      },
                    ]
                  },
                ] : allMenuTreeList
              },
            ],
          }
        ]
        const handleFormatMenuTreeList = (menuTreeList: MenuTree[], parentId: string = '') => {
          for (let i = 0; i < menuTreeList.length; i++) {
            const menuTree: MenuTree = menuTreeList[i]
            menuTree.id = (parentId ? parentId + '-' : parentId) + i
            menuTree.name = store.state.label[menuTree.nameKey || '']
            if ((menuTree.children || []).length > 0) {
              handleFormatMenuTreeList(menuTree.children || [], menuTree.id)
            }
          }
        }
        handleFormatMenuTreeList(menuTreeList, '')
        store.commit("setMenuTreeList", menuTreeList)
        store.commit("setUser", user)
        store.commit("setRoleCodeList", result.data.user.roleList.map((t: any) => t.roleCode))
        refreshRouter(router)
        handleToWhatYouWant()
      })
      .catch((res) => {
        if (74004 === res.code && retry) {
          setDeviceId("");
          setSalt("");
          initClient().then((result: DataResult<any>) => {
            handleSubmit(false);
          });
        }
        loginState.value = false
      })
    } else {
      return false
    }
  })
}
</script>

<style scoped lang="scss">
.login-wrap {
  position: absolute;
  width: 100%;
  height: 100%;
  background-size: 100%;
  display: flex;
  align-content: center;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;

  .ms-login {
    display: inherit;
    align-content: center;
    justify-content: center;
    align-items: center;
    flex-wrap: wrap;
    width: 500px;
    height: 350px;
    min-height: 380px;
    border-radius: 8px;
    background: rgba(255, 255, 255, 0.3);

    .ms-title {
      display: block;
      width: 80%;
      text-align: center;
      font-size: 20px;
      color: #fff;
      border-bottom: 1px solid #ddd;
    }

    .ms-content {
      width: 80%;
      margin-top: 30px;
    }

    .login-btn {
      width: 100%;
      height: 36px;
      margin-bottom: 10px;
    }

    .login-tips {
      font-size: 12px;
      line-height: 30px;
      color: #fff;
    }

    .login-helper {
      margin-left: 30px;
    }
  }
}
</style>
