interface ImportMetaEnv {
    readonly VITE_ENV: string
    readonly VITE_MODULE: string
}

interface ImportMeta {
    readonly env: ImportMetaEnv
}