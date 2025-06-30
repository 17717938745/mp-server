import {BreadCrumb, MenuTree, SidebarTree} from '@/typing/ma/System'
import {RouteRecordRaw} from 'vue-router';
import {capitalize, unCapitalize} from './StringUtil';

export const menuTreeToBreadCrumbList = (
    menuTreeList: MenuTree[],
    pathList: string[],
    parentPathList: string[] = [],
    breadCrumbList: BreadCrumb[] = []
): BreadCrumb[] => {
  if (pathList.length > 0) {
    const path = pathList[0];
    menuTreeList.forEach((temp) => {
      if (temp.path === path) {
        breadCrumbList.push({
          path: temp.path,
          link: temp.component
              ? '/' + new Array<string>().concat(parentPathList).concat(temp.path).join('/')
              : '',
          name: temp.name || '',
        });
        if (temp.children && pathList.length > 1) {
          menuTreeToBreadCrumbList(
              temp.children,
              pathList.slice(1),
              new Array<string>().concat(parentPathList).concat(temp.path),
              breadCrumbList
          );
        }
      }
    });
  }
  return breadCrumbList;
};

export const getFullSignUri = (): string => {
  const signUri = getSignUri()
  if (location.pathname === signUri) {
    return '/' + location.href.split('://')[1].split('/').slice(1).join('/')
  }
  const goBack = getFullUri()
  const uriTail = encodeURIComponent(signUri) === goBack ? '' : ('?' + 'goBack' + '=' + encodeURIComponent(getFullUri()))
  return signUri + uriTail
};

/**
 * default sign uri: /${module}/sign, example: /ma/sign
 */
export const getSignUri = (): string => {
  const pathArr: string[] = location.pathname.split('/').filter((t) => t)
  return '/' + pathArr[0] + '/sign'
};

export const getFullUri = (): string => {
  const uri = '/' + location.href.split('://')[1].split('/').slice(1).join('/')
  const arr = uri.split('?') || ['']
  const params = arr.length <= 1 ? '' : arr[1].split('&').filter(k => k.split('=').length > 0 && k.split('=')[0] !== 'goBack').join('&')
  const goBack = arr[0] + (params ? ('?' + params) : '')
  // console.log(`uri: ${uri}, goBack: ${goBack}`);
  return encodeURIComponent(goBack)
};
// 分销登录地址（杭州市民卡）
export const getDistributionSignUrl = () => {
  const signPath = '/html5/distribution/DisRegistration';
  return signPath + '?' + 'goBack' + '=' + encodeURIComponent(getFullUri());
};

export const menuTreeListToSidebarTreeList = (
    menuTreeList: MenuTree[],
    pathList: string[] = [],
    rootLevel: number = 0,
    rootSidebar: SidebarTree[] = [],
    sidebarList: SidebarTree[] = []
): SidebarTree[] => {
  const sidebarTreeList: SidebarTree[] = menuTreeList.map((temp) => {
    const sidebarTree = new SidebarTree(temp.id, temp.name || temp.path);
    sidebarTree.nameKey = temp.nameKey
    sidebarList.push(sidebarTree);
    sidebarTree.pathList = new Array<string>().concat(pathList).concat([temp.path]);
    sidebarTree.children = [];
    sidebarTree.showFlag = temp.showFlag;
    sidebarTree.icon = temp.icon || ''
    sidebarTree.hasComponent = (temp.component || '').length > 0
    if (temp.children) {
      menuTreeListToSidebarTreeList(
          temp.children,
          sidebarTree.pathList,
          rootLevel,
          rootSidebar,
          sidebarTree.children
      );
    }
    return sidebarTree;
  });
  if (pathList.length === rootLevel) {
    rootSidebar.push(...sidebarTreeList);
  }
  return rootSidebar;
};

export const shownMenuTreeList = (list: MenuTree[]) => {
  return list.filter(t => {
    const showFlag: boolean = t.showFlag
    if (!showFlag) {
      return showFlag
    }
    const children: MenuTree[] = t.children || []
    if (children.length > 0) {
      t.children = shownMenuTreeList(children)
    }
    if ((t.children || []).length === 0) {
      return (t.component || '').length > 0
    }
    return showFlag
  })
}
export const shownSidebarList = (list: SidebarTree[]) => {
  return list.filter(t => {
    const showFlag: boolean = t.showFlag
    if (!showFlag) {
      return showFlag
    }
    const children: SidebarTree[] = t.children || []
    if (children.length > 0) {
      t.children = shownSidebarList(children)
    }
    if ((t.children || []).length === 0) {
      return t.hasComponent
    }
    return showFlag
  })
}
export const moduleMapToMenuTreeList = (
    moduleMap: Record<string,
        () => Promise<{
          [key: string]: any;
        }>>,
    basePathList: string[]
): MenuTree[] => {
  const menuTreeList: MenuTree[] = [];
  let count = 0;
  const basePathLength = basePathList.length;
  const pathSetOfLevel: Record<number, Record<string, MenuTree>> = {};
  Object.keys(moduleMap).forEach((key) => {
    const filePathList = key.split('/').slice(basePathLength);
    for (let i = 0; i < filePathList.length; i++) {
      const path: string = unCapitalize(filePathList[i].split('.')[0]);
      const pathList = i === 0 ? [path] : filePathList.slice(0, i).concat(path);
      if (!pathSetOfLevel[i]) {
        pathSetOfLevel[i] = {};
      } else if (pathSetOfLevel[i][path]) {
        if (JSON.stringify(pathSetOfLevel[i][path].pathList) === JSON.stringify(pathList)) {
          continue;
        }
      }
      const menuTree: MenuTree = {
        id: String(++count),
        pathList: pathList,
        path: path,
        name: path,
        component: containsComponent(moduleMap, basePathList, filePathList.slice(0, i + 1))
            ? path
            : '',
        children: [],
        showFlag: true,
      };
      pathSetOfLevel[i][path] = menuTree;
      if (i === 0) {
        menuTreeList.push(menuTree);
      } else {
        const parentPath: string = filePathList[i - 1];
        const parentMenuTree: MenuTree = pathSetOfLevel[i - 1][parentPath];
        if (parentMenuTree) {
          if (!parentMenuTree.children) {
            parentMenuTree.children = [menuTree];
          } else {
            parentMenuTree.children.push(menuTree);
          }
        }
      }
    }
  });
  return menuTreeList;
};

export const menuTreeListToRouterTreeList = (
    moduleMap: Record<string,
        () => Promise<{
          [key: string]: any;
        }>>,
    bashPathList: string[],
    menuTreeList: MenuTree[],
    parentRouteList: RouteRecordRaw[] = [],
    pathList: string[] = [],
    absolutePathList: string[] = [],
    pathPrefix: string = '/',
    setTitle: boolean = false
): RouteRecordRaw[] => {
  menuTreeList.forEach((temp) => {
    if (temp.component) {
      const path: string = pathPrefix + new Array<string>().concat(pathList).concat(temp.path).join('/')
      const absolutePathArr: string[] = new Array<string>().concat(absolutePathList).concat(temp.component)
      const absolutePathString: string = `/${absolutePathArr.join('/')}`
      const name: string = `/${absolutePathArr.join('/')}`
      const route: RouteRecordRaw = {
        path: path,
        name: name,
        meta: {
          keepAlive: true,
          // 页面标题
          title: setTitle ? temp.name || temp.path : '',
          nameKey: temp.nameKey,
          path: absolutePathString,
          name: name,
          componentName: name,
        },
        component: getComponent(
            moduleMap,
            bashPathList,
            absolutePathArr,
            name
        ),
      };
      parentRouteList.push(route);
      if (!route.children) {
        route.children = [];
      }
      if (temp.children) {
        menuTreeListToRouterTreeList(
            moduleMap,
            bashPathList,
            temp.children,
            route.children,
            [],
            new Array<string>().concat(absolutePathList).concat(temp.path),
            '',
            setTitle
        );
      }
    } else {
      if (temp.children) {
        menuTreeListToRouterTreeList(
            moduleMap,
            bashPathList,
            temp.children,
            parentRouteList,
            new Array<string>().concat(pathList).concat(temp.path),
            new Array<string>().concat(absolutePathList).concat(temp.path),
            pathPrefix,
            setTitle
        );
      }
    }
  });
  return parentRouteList;
};

export const moduleMapToRouterTreeList = (
    moduleMap: Record<string,
        () => Promise<{
          [key: string]: any;
        }>>,
    basePathList: string[]
): RouteRecordRaw[] => {
  const menuTreeList = moduleMapToMenuTreeList(moduleMap, basePathList);
  // console.log(`============================================================================================================`)
  // console.log(`menuTreeList: ${JSON.stringify(menuTreeList, null, 2)}`)
  // console.log(`============================================================================================================`)
  return menuTreeListToRouterTreeList(moduleMap, basePathList, menuTreeList);
};

const getComponent = (
    moduleMap: Record<string,
        () => Promise<{
          [key: string]: any;
        }>>,
    bashPathList: string[],
    componentPath: string[],
    name: string
): any => {
  const path: string = getComponentPath(moduleMap, bashPathList, componentPath);
  if (!moduleMap[path]) {
    // console.log(`not found component from moduleMap, path: ${path}, keys of moduleMap: ${Object.keys(moduleMap)}`)
  }
  // console.log(`path: ${path}`)
  // return defineAsyncComponent(() => import(path))
  const component = moduleMap[path]
  if (component) {
    component().then(c => {
      if (c.default && !c.default.name && name) {
        c.default.name = name
      }
    })
  }
  return component
}

const containsComponent = (
    moduleMap: Record<string,
        () => Promise<{
          [key: string]: any;
        }>>,
    bashPathList: string[],
    componentPath: string[]
): any => {
  const path: string = getComponentPath(moduleMap, bashPathList, componentPath);
  return moduleMap.hasOwnProperty(path);
};

const getComponentPath = (
    moduleMap: Record<string,
        () => Promise<{
          [key: string]: any;
        }>>,
    bashPathList: string[],
    componentPath: string[]
): any => {
  return bashPathList
  .concat(...componentPath.slice(0, componentPath.length - 1))
  .concat(
      componentPath[componentPath.length - 1].indexOf('.') >= 0
          ? componentPath[componentPath.length - 1]
          : capitalize(componentPath[componentPath.length - 1]) + '.vue'
  )
  .join('/');
};

export function getParams() {
  var result: {
    [key: string]: string;
  } = {};
  const str = window.location.search;
  if (str.startsWith('?')) {
    const strParams = str.split('?')[1];
    const arrParams = strParams.split('&');
    //然后进行for循环，这里直接用了forEach
    arrParams.forEach((item) => {
      const temKey = item.split('=')[0];
      const temVal = item.split('=')[1];
      result[temKey] = decodeURIComponent(temVal);
    });
  }
  return result;
}

export function getParam(key: string): string {
  const val = getParams()[key];
  return val ? decodeURIComponent(val) : val;
}

/**
 * 获取默认跳转路径
 * @param list 菜单数列表
 * @param parentPath 上级目录
 */
export const getFirstPath = (list: MenuTree[], parentPath: string = '/ma'): string => {
  const cl = (list || [])
  let children
  if (cl.length > 0 && (children = cl[0].children || []).length > 0) {
    return getFirstPath(children, parentPath + '/' + children[0].path)
  } else {
    return parentPath
  }
}

// 判断url开头
export const mapUrl = (url: string) => {
  let rules = /(http|https):\/\/\S*/;
  if (rules.test(url)) {
    return url;
  } else {
    console.log('/', url);
    return `${location.origin}${url}`;
  }
}

// 将query对象转成 url参数 ?a=''&b=""
export const queryToUrl = (obj: { [k: string]: any }): string => {
  let url = '';
  const keys = Object.keys(obj);
  if (keys.length) {
    for (let i in obj) {
      url += i + '=' + obj[i] + (i != keys[keys.length - 1] ? '&' : '');
    }
    return '?' + url;
  }
  return url;
}
