<template>
  <div>
    <el-form ref="form" label-width="80px" :model="form">
      <el-form-item label="活动名称">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="活动区域">
        <el-select v-model="form.region" placeholder="请选择活动区域">
          <el-option label="区域一" value="shanghai" />
          <el-option label="区域二" value="beijing" />
        </el-select>
      </el-form-item>
      <el-form-item label="活动时间">
        <el-col :span="11">
          <el-date-picker
            v-model="form.date1"
            placeholder="选择日期"
            style="width: 100%"
            type="date"
          />
        </el-col>
        <el-col class="line" :span="2">-</el-col>
        <el-col :span="11">
          <el-time-picker
            v-model="form.date2"
            placeholder="选择时间"
            style="width: 100%"
          />
        </el-col>
      </el-form-item>
      <el-form-item label="即时配送">
        <el-switch v-model="form.delivery" />
      </el-form-item>
      <el-form-item label="活动性质">
        <el-checkbox-group v-model="form.type">
          <el-checkbox label="美食/餐厅线上活动" name="type" />
          <el-checkbox label="地推活动" name="type" />
          <el-checkbox label="线下主题活动" name="type" />
          <el-checkbox label="单纯品牌曝光" name="type" />
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="特殊资源">
        <el-radio-group v-model="form.resource">
          <el-radio label="线上品牌商赞助" />
          <el-radio label="线下场地免费" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="活动形式">
        <el-input v-model="form.desc" type="textarea" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  export default {
    name: '',
    props: {
      nodeData: {
        type: Object,
        default: () => {},
      },
      lf: {
        type: Object || String,
        default: () => {},
      },
    },
    data() {
      return {
        form: {
          name: '',
          region: '',
          date1: '',
          date2: '',
          delivery: false,
          type: [],
          resource: '',
          desc: '',
        },
      }
    },
    mounted() {
      const { properties } = this.$props.nodeData
      if (properties) {
        this.$data.form = Object.assign({}, this.$data.form, properties)
      }
    },
    methods: {
      onSubmit() {
        console.log('submit!')
        const { id } = this.$props.nodeData
        this.$props.lf.setProperties(id, this.$data.form)
        this.$emit('onClose')
      },
    },
  }
</script>
<style scoped></style>
