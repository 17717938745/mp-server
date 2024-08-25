export default function getImg(name: string) {
  return `/third/img/${name}`
  // return (name ? new URL(`/src/asset/img/${name}`, import.meta.url).href : '')
}
