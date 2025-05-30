<template>
  <div>
    <div class="query-container">
      <div class="query-btn">
        <el-button
            :submitDisable="submitDisable"
            :icon="formData.forumId ? Edit : Plus"
            @click="handleMerge"
            :type="formData.forumId ? 'warning' : 'success'"
        >
          {{ formData.forumId ? 'Update' : 'Save' }}
        </el-button>
        <el-button
            v-if="!props.afterSuccess"
            @click="props.goBack? props.goBack() : router.back()"
        >
          Go back
        </el-button>
      </div>
    </div>
    <div class="flex justify-start items-center">
      <el-form
          :model="formData"
          :rules="rules"
          ref="formRef"
          label-width="0px"
          class="ms-content"
          @submit.native.prevent
      >
        <el-form-item prop="title">
          <el-input
              v-model="formData.title"
              style="width: 100%"
              class="title"
              :placeholder="store.state.label.pleaseInputTitle"
          />
        </el-form-item>
        <el-form-item class="lead-header" prop="content">
          <!--Download tinymceScriptSrc from tiny.cloud: https://www.tiny.cloud/my-account/downloads/, and github: https://github.com/tinymce/tinymce/tags-->
          <editor
              style="width: 100%;"
              api-key="uzd5kec740x8lw118ozl7qsq9040f1h2z4f65f7zkrxh12mn"
              v-model="formData.content"
              :init="editConfig"
              tinymceScriptSrc="/third/tinymce@7.7.2/tinymce.min.js"
              model-events="change keydown blur focus"
              output-format="html"
              cloud-channel="5-stable"
          />
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script lang="tsx" setup>
import {onMounted, reactive, ref, toRefs, watch, watchEffect,} from 'vue'
import {Store, useStore} from 'vuex'
import {useRouter} from 'vue-router'
import Editor from '@tinymce/tinymce-vue'
import {httpGet, httpPutJson, httpUpload,} from '@/util/HttpUtil'
import {Edit, Plus} from '@element-plus/icons-vue'
import {ElMessage} from 'element-plus'
import {DataResult} from '@/typing/ma/System'
import {StoreType} from '@/store/Industry'

interface PropType {
  forumId?: string
  goBack?: Function
  afterSuccess?: Function
  category?: string
}

const props = withDefaults(defineProps<PropType>(), {
  forumId: '',
  category: '',
})


const formRef = ref(null)
const store: Store<StoreType> = useStore()
const router = useRouter()
const formData = ref({
  forumId: props.forumId,
  h5Id: '',
  title: '',
  content: '',
  category: props.category,
})
const state = reactive({
  submitDisable: false,
  rules: {
    title: [
      {
        required: true,
        message: "Please input",
        trigger: "blur",
      },
    ],
  },
  editConfig: {
    height: 1024,
    selector: 'textarea',
    license_key: 'uzd5kec740x8lw118ozl7qsq9040f1h2z4f65f7zkrxh12mn',
    body_class: 'douson-h5',
    content_css: ['/third/css/report.css'],
    menubar: "",
    language: "zh_CN",
    branding: false,
    convert_urls: false,
    paste_data_images: true,
    fontsize_formats: "12px 14px 16px 18px 20px 24px 26px 28px 30px 32px 36px",
    font_formats:
        "Times New Roman=times new roman;微软雅黑=Microsoft YaHei,Helvetica Neue;PingFang SC;sans-serif;苹果苹方=PingFang SC,Microsoft YaHei,sans-serif;宋体=simsun;serifsans-serif;Terminal=terminal;monaco;times",
    plugins: "autolink lists link image anchor media table fullscreen",
    toolbar: [
      "h1 h2 h3 h4 h5 h6",
      "fontselect fontsizeselect | bold italic underline strikethrough blockquote | forecolor backcolor |",
      "alignleft aligncenter alignright alignjustify | lineheight | numlist bullist | fullWidthImage table | link",
      // 'fullWidthImage link image media table',
      "undo redo removeformat fullscreen",
    ],
    // file_picker_types: 'image',
    // images_upload_credentials: true,
    file_picker_callback: function (callback, value, meta) {
      // Provide image and alt text for the image dialog
      console.log(`meta.filename: ${meta.filename} ${meta.filetype}`);
      if (meta.filetype == "image") {
        callback("img.jpg", {
          alt: 'My alt text' /*, class: 'full-width'*/,
          width: '100%',
        });
      }
    },
    images_upload_handler: (blobInfo, progress) => new Promise((resolve, reject) => {
      const formData = new FormData()
      formData.set("file", blobInfo.blob())
      formData.set("filename", blobInfo.filename)
      httpUpload(`index/img`, formData).then(res => {
        ElMessage.success(`Image upload success`)
        resolve(res.data.url, {
          src: res.data.url,
          width: '100%',
        })
      })
      .catch((err) => {
        reject(`Image upload error: ${err}`)
      })
    }),
    setup: function (editor) {
      let img;
      editor.ui.registry.addToggleButton('fullWidthImage', {
        icon: 'image',
        text: '',
        tooltip: "100%宽度",
        disabled: false,
        onAction: function (_) {
          // noinspection TypeScriptUnresolvedFunction
          if (img) {
            const width = img.getAttribute("width");
            if (width != "100%") {
              img.setAttribute("width", "100%");
            } else {
              img.removeAttribute("width");
            }
          } else {
            img = document.createElement("img");
            img.setAttribute("src", '/third/svg/link.svg')
            img.setAttribute("width", "100%");
            // img.setAttribute('class', fullWidthClass)
            editor.insertContent(img.outerHTML);
          }
        },
        onSetup: function (buttonApi) {
          const editorEventCallback = function (eventApi) {
            if (eventApi.element.nodeName.toLowerCase() === 'img') {
              img = eventApi.element
            } else {
              img = null
            }
            // buttonApi.setDisabled(eventApi.element.nodeName.toLowerCase() !== 'img');
          };
          editor.on("NodeChange", editorEventCallback);
          return function (buttonApi) {
            console.log(`on off ... ${buttonApi}`);
            editor.off("NodeChange", editorEventCallback);
          };
        },
      });
      // noinspection TypeScriptUnresolvedFunction
      editor.ui.registry.addButton('twoImage', {
        icon: "insert-time",
        text: "插入双图片",
        tooltip: "插入双图片",
        disabled: true,
        onAction: function (_) {
          // noinspection TypeScriptUnresolvedFunction
          editor.insertContent(
              '<div class="two-image">' +
              '<img alt="img" src="https://img1.baidu.com/it/u=4156257074,1029422296&fm=26&fmt=auto"/>' +
              '<img alt="img" src="https://img1.baidu.com/it/u=4156257074,1029422296&fm=26&fmt=auto"/>' +
              "</div>"
          );
        },
        onSetup: function (buttonApi) {
          const editorEventCallback = function (eventApi) {
            // noinspection TypeScriptUnresolvedFunction
            buttonApi.setDisabled(eventApi.element.nodeName.toLowerCase() === "time");
          };
          editor.on("NodeChange", editorEventCallback);
          return function (buttonApi) {
            editor.off("NodeChange", editorEventCallback);
          };
        },
      });
      // noinspection TypeScriptUnresolvedFunction,TypeScriptUnresolvedVariable
      editor.ui.registry.addButton("twoTwoImage", {
        icon: "insert-time",
        text: "插入2行双图片",
        tooltip: "插入2行双图片",
        disabled: true,
        onAction: function (_) {
          // noinspection TypeScriptUnresolvedFunction
          editor.insertContent(
              '<div class="two-two-image">' +
              "<div>" +
              '<img alt="img" src="https://img1.baidu.com/it/u=4156257074,1029422296&fm=26&fmt=auto"/>' +
              '<img alt="img" src="https://img1.baidu.com/it/u=4156257074,1029422296&fm=26&fmt=auto"/>' +
              "</div>" +
              "<div>" +
              '<img alt="img" src="https://img1.baidu.com/it/u=4156257074,1029422296&fm=26&fmt=auto"/>' +
              '<img alt="img" src="https://img1.baidu.com/it/u=4156257074,1029422296&fm=26&fmt=auto"/>' +
              "</div>" +
              "</div>"
          );
        },
        onSetup: function (buttonApi) {
          const editorEventCallback = function (eventApi) {
            // noinspection TypeScriptUnresolvedFunction
            buttonApi.setDisabled(eventApi.element.nodeName.toLowerCase() === "time");
          };
          editor.on("NodeChange", editorEventCallback);
          return function (buttonApi) {
            editor.off("NodeChange", editorEventCallback);
          };
        },
      });
    },
  },
});

const {
  submitDisable,
  rules,
  editConfig,
} = {...toRefs(state)};
onMounted(() => {
});

const handleValueChange = () => {
      // console.log(`arguments.length: ${arguments.length}`)
    },
    handleMerge = () => {
      formRef.value.validate((valid: any) => {
        if (valid) {
          state.submitDisable = true
          httpPutJson(`index/h5`, formData.value).then((r: any) => {
            formData.value.h5Id = r.data.h5Id
            httpPutJson("forum/merge", formData.value)
            .then((result: DataResult<any>) => {
              ElMessage.success("Merge success")
              formData.value.forumId = result.data.forumId
              state.submitDisable = false
              if (props.afterSuccess) {
                props.afterSuccess(formData.value)
              }
            })
            .catch((r: any) => {
              state.submitDisable = false
            })
          })
          .catch((r: any) => {
            state.submitDisable = false
          })
        }
      })
    },
    handleGoBack = () => {
      router.back()
    },
    handleGet = () => {
      if (formData.value.forumId) {
        httpGet("forum", {
          forumId: formData.value.forumId,
        })
        .then((result: DataResult<any>) => {
          ElMessage.success("Query success")
          formData.value = Object.assign({}, result.data || {})
        })
      }
    }
watchEffect(() => {
  // console.log(`html change: ${state.param.viewDetail.html}`)
});
watch(
    () => props.forumId,
    (t: string) => {
      formData.value.forumId = t
      handleGet()
    },
    {
      immediate: true,
    }
)
</script>

<style lang="scss">
.tox-promotion {
  display: none !important;
}
</style>
