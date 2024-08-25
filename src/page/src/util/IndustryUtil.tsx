import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import {includes} from '@/util/ArrayUtil'

const handleColumnConfigList = (configList: ViewConfig[] = [], excludeTypeList: ValueType[] = [], deep: boolean = false, addSelf: boolean = false, gl: any[] = [], cgl: any[] = [], level: number = 0) => {
  gl = gl || []
  cgl = cgl || []
  const list: ViewConfig[] = [];
  (configList || [])
  .filter((t: ViewConfig) => !t.hide)
  .filter((t: ViewConfig) => !includes(excludeTypeList, t.type))
  .filter((t: ViewConfig) => !includes(gl, t.labelKey || t.label || '') || includes(cgl, t.labelKey || t.label || ''))
  .forEach(t => {
    const len = (t.children || []).length
    if (len === 0 || addSelf) {
      list.push(t)
    }
    if (deep && len > 0) {
      list.push(...handleColumnConfigList((t.children || []), excludeTypeList, deep, addSelf, gl, cgl, level + 1))
    }
  })
  return list
}

export {
  handleColumnConfigList,
}
