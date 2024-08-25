import { StoreOptions } from 'vuex'
import { getDeviceId, getSalt, setDeviceId, setSalt } from '../util/StorageUtil'


export interface StoreType {
    deviceId: string
    salt: string
}

// noinspection JSUnusedGlobalSymbols
export const storeOptions: StoreOptions<StoreType> = {
    state: {
        deviceId: getDeviceId(),
        salt: getSalt(),
    },
    mutations: {
        setDeviceId(state: StoreType, data: string) {
            state.deviceId = data
            setDeviceId(data)
        },
        setSalt(state: StoreType, data: string) {
            state.salt = data
            setSalt(data)
        },
    },
    actions: {
    },
    modules: {},
}

