import {SHA1, enc} from 'crypto-js'

const sha1Hex = function (str: string) {
    return SHA1(str).toString(enc.Hex)
}

export {
    sha1Hex
}
export default () => {
    return 'CipherUtil.tsx';
}