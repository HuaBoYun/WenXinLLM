<template>
  <el-table-column
    v-if="!show"
    align="center"
    :label="item.name"
    :prop="item.field"
    show-overflow-tooltip
    #default="{ row }"
  >
    <slot>{{ filterVal(row) }}</slot>
  </el-table-column>
  <div v-else>
    <slot></slot>
  </div>
</template>

<script>
  export default {
    name: 'TableColumns',
    props: ['item', 'show'],
    data() {
      return {
        textVal: '',
      }
    },
    mounted() {},
    methods: {
      filterVal(row) {
        if (this.item) {
          const field = this.item.field || undefined
          const componentType = this.item.componentType || undefined
          const extJson = this.item.extJson || '[]'
          const attachedField = this.item.attachedField || undefined
          switch (componentType) {
            case 'Cselect':
            case 'Cradio':
            case 'Ccheckbox': {
              const datas = JSON.parse(extJson)
              const textVal =
                (datas.find((x) => x.value === row[field]) || {}).label || ''
              return textVal || ''
            }
            case 'CselectDepartment':
            case 'CselectDepartments':
            case 'CselectPerson':
            case 'CselectPeople':
            case 'CselectConmpany':
            case 'CselectConmpanys':
            case 'Cskht':
            case 'Cydskx':
            case 'Cfkyhzh':
            case 'Cskyhzh':
            case 'Cfph':
            case 'Chtmc':
            case 'Cpjdx':
            case 'Cpjmb':
            case 'Ccsmb': {
              const textVal = row[attachedField] || ''
              return textVal || ''
            }
            default:
              return row[field] || ''
          }
        }
      },
    },
  }
</script>
