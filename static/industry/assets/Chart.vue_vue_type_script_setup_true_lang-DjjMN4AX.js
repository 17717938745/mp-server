import{h as c}from"./main-pnK9NfqU.js";const v=Vue.defineComponent({__name:"Chart",setup(d){VueRouter.useRouter(),Vuex.useStore().state.user;const a=Vue.ref(null);Mermaid.initialize({startOnLoad:!1,theme:"default",flowchart:{curve:"basis",target:"_blank"},securityLevel:"loose"});const o=Vue.ref(`
graph TD
    A[Vue 应用] --> B{Mermaid 图表}
    B --> C[流程图]
    B --> D[序列图]
    B --> E[甘特图]
    style A fill:#4CAF50,color:white
`),s=async()=>{try{const{svg:r}=await Mermaid.render("mermaid-svg",o.value);a.value&&(a.value.innerHTML=r)}catch(r){console.error("Mermaid 渲染错误:",r)}};Vue.onMounted(()=>{});const l=(r,t=[],n="",u=[])=>(r.forEach(e=>{t.push(n?`${n} --> ${e.id}[${e.label}
（${e.totalUserCount}人）]`:`${e.id}[${e.label}
（${e.totalUserCount}人）]`),e.children&&e.children.length>0&&l(e.children,t,e.id,u)}),t),i=Vue.ref([]);return c("/system/depart/list",{}).then(r=>{i.value=r.list;const t=[],n=[];l(i.value,t,"",n),o.value=`
flowchart TD
    ${t.join(`
    `)}

    ${n.join(`
    `)}

`,console.log(o.value),s()}),(r,t)=>(Vue.openBlock(),Vue.createElementBlock("div",null,[Vue.createElementVNode("div",{ref_key:"mermaidContainer",ref:a,class:"mermaid-container"},null,512)]))}});export{v as _};
