import {createVNode, render} from "vue";
import Toast from "./Toast.vue";

const div = document.createElement("div")
div.setAttribute("class", "toast")
document.body.appendChild(div)

let textList: string[] = []
let time: any = null
export const toast = (text: string, duration: number = 3000) => {
  // textList.push(text)
  if (null != time) {
    clearTimeout(time)
  }
  // render(createVNode(Toast, {text: textList.join('\n')}), div)
  render(createVNode(Toast, {text: text}), div)
  time = setTimeout(() => {
    render(null, div)
    // textList.splice(0)
  }, duration)

}
