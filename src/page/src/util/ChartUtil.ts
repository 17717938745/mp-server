
/*
 * @Author: YourName
 * @Date: 2022-02-23 10:38:51
 * @LastEditTime: 2024-02-21 10:37:41
 * @LastEditors: 韦玮莹
 * @Description: 
 * @FilePath: \h5\src\util\ChartUtil.tsx
 * 版权声明
 */
/**
 * @description: 将接口返回的数据组装成chart能展示的格式
 * @param {*} list 原始数组
 * @param {*} Key 从接口中要获取的x轴的值的key
 * @param {*} _color 颜色
 * @return {*} 返回series数组 series:[{color,data:[]}]
 * @use: 
 */
import { formatUserDate } from '@/util/DateUtil'
interface IMakerObject {
  markerData: string[]; // 里面的这些点加标记
  markerKey: string;// 对比的字段
  markerStyle: object;// marker的样式
  dataLabels: object;
}
export function formatChartSeriesItem(list: any[], Key: string, _color?: string, name = '', others: any = {}) {
  let data: any[] = [];
  if (list && list.length) {
    list.forEach((item, index) => {

      if (!others.markerObject) {
        data.push(Number(item[Key]))
      } else if (others.markerObject) {
        // console.log(others.markerObject.markerKey, others.markerObject.markerData, item[others.markerObject.markerKey])
        if (others.markerObject.markerData.includes(item[others.markerObject.markerKey])) {
          // 有maker
          data.push({
            y: Number(item[Key]),
            marker: others.markerObject.markerStyle,
            dataLabels: others.markerObject.dataLabels
          })
        } else {
          data.push(Number(item[Key]))
        }
      }

    })
  }
  // console.log(data, data)
  return {
    name,
    color: _color,
    data,
    marker: {
      radius: 1
    },
    ...others
  }
}



// {list:[],key:'',color:''}
export function formatChartSeries(seriesOprionArray: any[] = []) {
  if (seriesOprionArray && seriesOprionArray.length) {
    const series = seriesOprionArray.map((item: any) => {
      return formatChartSeriesItem(item.list, item.key, item.color, item.name, item.others)
    })
    // console.log('series', series)
    return series
  }

}


function formatDate(date: string) {
  if (date.includes('-')) return date;
  else { return formatUserDate(date) }
}
export function formatCategories(list: any[], key: string) {
  return list.map((i) => {
    return formatDate(i[key])
  })
}