import { httpLogin } from "../api/mp/index";
import { DataResult } from "../typing/ma/System";
// 用户登录

export const handleLogin = () => {
  return new Promise((resolve, reject) => {
    uni.login({
      success(res: DataResult<any>) {
        if (res.code) {
          httpLogin(res.code).then((result) => {
            resolve(result)
          })
        } else {
          reject()
        }
      },
    });
  })

};


// 获取用户信息