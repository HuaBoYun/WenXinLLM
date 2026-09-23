<template>
  <component
    class="myComponent"
    :is="item.componentType"
    :item="item"
    :form="form"
    @cchange="handleChange"
  ></component>
</template>

<script>
  import {
    Cinput,
    Cselect,
    Ctarea,
    CselectPerson,
    CselectDepartment,
    CmultipleSelect,
    Cradio,
    Ccheckbox,
    Cnumber,
    CdatePicker,
    CmultipleDatePicker,
    CdateRange,
    CmultipleDateRange,
    CselectDepartments,
    CselectPeople,
    CselectConmpany,
    CselectConmpanys,
    Cskht,
    Cydskx,
    Cfkyhzh,
    Cskyhzh,
    Cfph,
    Chtmc,
    Cpjdx,
    Cpjmb,
    Cyear,
    Ccsmb,
  } from './fields/index'

  export default {
    components: {
      Cinput,
      Ctarea,
      Cselect,
      CselectPerson,
      CselectDepartment,
      CmultipleSelect,
      Cradio,
      Ccheckbox,
      Cnumber,
      CdatePicker,
      CmultipleDatePicker,
      CdateRange,
      CmultipleDateRange,
      CselectDepartments,
      CselectPeople,
      CselectConmpany,
      CselectConmpanys,
      Cskht,
      Cydskx,
      Cfkyhzh,
      Cskyhzh,
      Cfph,
      Chtmc,
      Cpjdx,
      Cpjmb,
      Cyear,
      Ccsmb,
    },
    props: ['item', 'form'],
    data() {
      return {}
    },
    methods: {
      handleChange(val, field) {
        this.$emit('cchange', val, field)
      },
    },
  }
</script>
