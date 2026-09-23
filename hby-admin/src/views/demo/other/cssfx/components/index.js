const requireEffect = require.context('./', false, /\.vue$/)
const effectList = requireEffect.keys()
const effects = {}
const components = {}
for (const filename of effectList) {
  const name = filename.replace('./', '').replace('.vue', '')
  const type = name.substring(0, name.indexOf('-'))
  const raw = require(`!!raw-loader!./${name}`).default
  const component = requireEffect(filename).default
  const html = /<template>(.*?)<\/template>/g
    .exec(JSON.stringify(raw))[0]
    .replace(/<\/?template>/g, '')
    .replace(/^\\n/, '')
    .replace(/\\n/g, '\n')
    .replace(/\\"/g, '"')
  const css = /<style scoped>(.*?)<\/style>/g
    .exec(JSON.stringify(raw))[0]
    .replace(/<\/?style(?: scoped)?>/g, '')
    .replace(/^\\n/, '')
    .replace(/\\n/g, '\n')
  effects[name] = { name, type, html, css }
  components[name] = component
}
export { effects, components }
