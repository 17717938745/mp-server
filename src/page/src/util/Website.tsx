
interface IItem {
    count: string;
    describe: string;
    icon: string;
    unit: string;
    value: string;
  }
export const handleFind = (list,value: string): IItem | undefined => {
    return list.find((item: IItem) => item.value === value);
  };

export const scrollTo = (id) => {
  let el: any = document.getElementById(id);
  if(!el)return;
  setTimeout(() => {
     console.log('要滚动了',el,el.offsetTop,document.documentElement.clientHeight)
    document.body.scrollTop = el.offsetTop
    document.documentElement.scrollTop = el.offsetTop;
  },500)
}