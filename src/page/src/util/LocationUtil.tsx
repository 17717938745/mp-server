import {isMp} from "./Platform";

export const getPathname = () => {
  if (isMp) {
    // @ts-ignore
    const pages: any[] = getCurrentPages()
    if (pages.length <= 0) {
      return ''
    } else {
      return pages[pages.length - 1].route.replace(/^page/, '')
    }
  } else {
    return location.pathname
  }
}