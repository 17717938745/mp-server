import {createStore} from 'vuex'
import {storeOptions as commonStoreOptions, StoreType as CommonStoreType} from './Common'
import {storeOptions as industryStoreOptions, StoreType as IndustryStoreType} from './Industry'

// @ts-ignore
export interface StoreType extends MaStoreType, Html5StoreType, CommonStoreType, WebsiteStoreType, IndustryStoreType {
}

export default createStore<StoreType>({
  // @ts-ignore
  state: Object.assign(
      {},
      industryStoreOptions.state || {},
      commonStoreOptions.state || {},
  ),
  mutations: Object.assign(
      {},
      industryStoreOptions.mutations || {},
      commonStoreOptions.mutations || {},
  ),
  actions: Object.assign(
      {},
      industryStoreOptions.actions || {},
      commonStoreOptions.actions || {},
  ),
  modules: Object.assign(
      {},
      industryStoreOptions.modules || {},
      commonStoreOptions.modules || {},
  ),
})
