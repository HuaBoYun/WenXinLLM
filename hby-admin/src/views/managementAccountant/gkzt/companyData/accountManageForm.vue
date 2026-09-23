<template>
  <el-form
    ref="form"
    label-width="100px"
    :disabled="true"
    :form="dataMap"
  >
    <el-row :gutter="14">
      <el-col :span="12" v-for="item in colList" :key="item.fid">
        <el-form-item :label="item.fname" :porp="item.oursColname">
          <el-input
            v-model="dataMap[item.oursColname]"
            clearable
            :placeholder="item.fname"
          />
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script>
  export default {
    name: 'accountManageForm',
    props: {
      colList: {
        type: Array,
        default: () => []
      },
      dataMap: {
        type: Object,
        default: () => ({})
      },
    },
    data() {
      return {
      }
    },
    created() {
    },
    methods: {
      
    },
  }
</script>
