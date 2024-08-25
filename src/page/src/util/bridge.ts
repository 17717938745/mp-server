import {getCookie} from "./StorageUtil";

// @ts-ignore
function setupWebViewJavascriptBridge(callback) {
  // console.log(`isApp: ${getCookie('leadAppType')}`)
  if (window && getCookie('leadAppType')) {
    if (window.WebViewJavascriptBridge) {
      return callback(window.WebViewJavascriptBridge);
    } else {
      document.addEventListener('WebViewJavascriptBridgeReady', function () {
        callback(window.WebViewJavascriptBridge)
      }, false);
      if (window.WVJBCallbacks) {
        return window.WVJBCallbacks.push(callback);
      }
      window.WVJBCallbacks = [callback];
      var WVJBIframe = document.createElement('iframe');
      WVJBIframe.style.display = 'none';
      WVJBIframe.src = 'wvjbscheme://__BRIDGE_LOADED__';
      document.documentElement.appendChild(WVJBIframe);
      setTimeout(function () {
        document.documentElement.removeChild(WVJBIframe)
      }, 0)
    }

  }
}

if (getCookie('leadAppType')) {
//初始化
  setupWebViewJavascriptBridge(function (bridge) {
    try {
      bridge.init(function (message, callback) {
        callback(null);
      })
    } catch (e) {
    }
  })
}
export default {
  //js调APP方法 （参数分别为:app提供的方法名  传给app的数据  回调）
  callhandler: function (method, params, callback) {
    setupWebViewJavascriptBridge(function (bridge) {
      bridge.callHandler(method, params, callback)
    })
  },

  // APP调js方法 （参数分别为:js提供的方法名  回调）
  registerHandler(method, callback) {
    setupWebViewJavascriptBridge((bridge) => {
      bridge.registerHandler(method, (data, responseCallback) => {
        callback(data, responseCallback)
      })
    })
  }
}