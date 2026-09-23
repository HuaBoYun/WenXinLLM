<template>
  <div class="custom-form-attr-view">
    <label class="widget-title">组件属性</label>
    <br />
    <br />
    <el-empty v-if="form === ''" description="未选择组件"></el-empty>
    <el-form v-else ref="form" :model="form" label-width="70px">
      <el-form-item label="标题">
        <el-input v-model="form.name" placeholder="请输入标题"></el-input>
      </el-form-item>
      <el-form-item label="占位提示">
        <el-input
          v-model="form.placeholder"
          placeholder="请输入占位提示"
        ></el-input>
      </el-form-item>
      <el-form-item label="宽度">
        <el-slider
          v-model="form.width"
          :step="10"
          :format-tooltip="formatTooltip"
          show-stops
        ></el-slider>
      </el-form-item>
      <el-form-item label="是否必填">
        <el-switch v-model="form.required"></el-switch>
      </el-form-item>
      <el-form-item v-if="form.dataType" label="数据类型">
        <el-radio-group v-model="form.dataType" size="mini">
          <el-radio-button label="local">静态数据</el-radio-button>
          <el-radio-button label="url">动态数据</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        v-if="form.dataType === 'local' && form.type !== 'cascader'"
        label="静态数据"
      >
        <el-input
          type="textarea"
          v-model="form.url"
          placeholder="请输入Json格式数据"
        ></el-input>
        <el-alert
          title="Json数据格式，标签名label，键值名value"
          type="warning"
          :closable="false"
        ></el-alert>
        <el-alert
          title='示例：{"value":"1", "label":"选项1"}'
          type="success"
          :closable="false"
        ></el-alert>
      </el-form-item>
      <el-form-item
        v-if="form.dataType === 'local' && form.type === 'cascader'"
        label="静态数据"
      >
        <el-input
          type="textarea"
          v-model="form.url"
          placeholder="请输入Json格式数据"
        ></el-input>
        <el-alert
          title="Json数据格式，标签名label，键值名value，子集键名children"
          type="warning"
          :closable="false"
        ></el-alert>
        <el-alert
          title='示例：{"value":"1", "label":"选项1", children: [{"value":"2", "label":"子选项1"}]}'
          type="success"
          :closable="false"
        ></el-alert>
      </el-form-item>
      <el-form-item
        v-if="form.dataType === 'url' && form.type !== 'cascader'"
        label="数据接口"
      >
        <el-input
          v-model="form.url"
          placeholder="请输入数据接口地址"
        ></el-input>
        <el-alert
          title="固定使用GET请求接口，数据位置data，键名id，值名value"
          type="warning"
          :closable="false"
        ></el-alert>
      </el-form-item>
      <el-form-item
        v-if="form.dataType === 'url' && form.type === 'cascader'"
        label="数据接口"
      >
        <el-input
          v-model="form.url"
          placeholder="请输入数据接口地址"
        ></el-input>
        <el-alert
          title="固定使用GET请求接口，数据位置data，标签名label，键值名value，子集键名children"
          type="warning"
          :closable="false"
        ></el-alert>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  export default {
    name: 'CustomFormAttr',
    data() {
      return {
        form: '',
      }
    },
    created() {
      let that = this
      this.$bus.$on('selectCustomerFormItem', (val) => {
        that.form = val
      })
    },
    methods: {
      formatTooltip(val) {
        return val + '%'
      },
    },
  }
</script>

<style scoped>
  .custom-form-attr-view {
    width: 400px;
    min-height: calc(100vh - 100px);
    padding: 5px;
  }
  .widget-title {
    font-size: 16px;
    font-weight: 600;
  }
</style>
