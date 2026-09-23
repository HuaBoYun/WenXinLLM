export function translateTitle(title) {
  if (this.$te(`vabI18n.${title}`)) return this.$t(`vabI18n.${title}`)
  return title
}
