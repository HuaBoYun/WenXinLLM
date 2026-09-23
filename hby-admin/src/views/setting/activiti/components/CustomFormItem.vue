<template>
  <div class="custom-form-item-view">
    <label class="widget-title">输入型组件</label>
    <draggable v-model="widgetList1" :options="{group:{name: 'itxst',pull:'clone', put: false},sort: false}"
               animation="300" :clone="clone">
      <transition-group class="widget-group">
        <div class="widget-view" v-for="item in widgetList1" :key="item.key">
          <div class="widget">
            <div>
              <vab-icon
                :icon="item.type"
                :is-custom-svg="true"
                style="font-size: 12px; margin-right: 5px"
              />
            </div>
            <div>{{ item.name }}</div>
          </div>
        </div>
      </transition-group>
    </draggable>
    <br>
    <label class="widget-title">选择型组件</label>
    <draggable v-model="widgetList2" :options="{group:{name: 'itxst',pull:'clone', put: false},sort: false}"
               animation="300" :clone="clone">
      <transition-group class="widget-group">
        <div class="widget-view" v-for="item in widgetList2" :key="item.key">
          <div class="widget">
            <div>
              <vab-icon
                :icon="item.type"
                :is-custom-svg="true"
                style="font-size: 12px; margin-right: 5px"
              />
            </div>
            <div>{{ item.name }}</div>
          </div>
        </div>
      </transition-group>
    </draggable>
  </div>
</template>

<script>
import draggable from 'vuedraggable';

export default {
  name: "CustomFormItem",
  components: {
    draggable
  },
  data() {
    return {
      widgetList1: [
        {key: 1, type: 'input', required: false, value: '', name: '单行文本', width: 100, placeholder: '请输入'},
        {key: 2, type: 'textarea', required: false, value: '', name: '多行文本', width: 100, placeholder: '请输入'},
        {key: 3, type: 'password', required: false, value: '', name: '密码', width: 100, placeholder: '请输入'},
        {key: 4, type: 'number', required: false, value: '', name: '计数器', width: 100, placeholder: '请输入'},
        {key: 5, type: 'editor', required: false, value: '', name: '富文本', width: 100, placeholder: '请输入'},
      ],
      widgetList2: [
        // local 本地数据  url 网络数据
        {key: 1, type: 'select', required: false, value: '', name: '下拉选择', width: 100, placeholder: '请选择', dataType: 'local', dataValue: '', url: ''},
        {key: 2, type: 'cascader', required: false, value: [], name: '级联选择', width: 100, placeholder: '请选择', dataType: 'local', dataValue: '', url: ''},
        {key: 3, type: 'radio', required: false, value: '', name: '单选框组', width: 100, placeholder: '请选择', dataType: 'local', dataValue: '', url: ''},
        {key: 4, type: 'checkbox', required: false, value: [], name: '多选框组', width: 100, placeholder: '请选择', dataType: 'local', dataValue: '', url: ''},
        {key: 5, type: 'switch', required: false, value: '', name: '开关', width: 100, placeholder: '请选择', min: 1, max: 100, step: 1},
        {key: 6, type: 'slider', required: false, value: '', name: '滑块', width: 100, placeholder: '请选择'},
        {key: 7, type: 'time', required: false, value: '', name: '时间选择', width: 100, placeholder: '请选择'},
        {key: 8, type: 'time-range', required: false, value: '', name: '时间范围', width: 100, placeholder: '请选择'},
        {key: 9, type: 'date', required: false, value: '', name: '日期选择', width: 100, placeholder: '请选择'},
        {key: 10, type: 'date-range', required: false, value: '', name: '日期范围', width: 100, placeholder: '请选择'},
        {key: 11, type: 'rate', required: false, value: '', name: '评分', width: 100, placeholder: '请选择'},
        {key: 12, type: 'color', required: false, value: '', name: '颜色选择', width: 100, placeholder: '请选择'},
        {key: 13, type: 'upload', required: false, value: '', name: '上传', width: 100, placeholder: '请选择'},
      ],
    }
  },
  methods: {
    clone(origin) {
      //通过转成字符串，让他变成一个新对象，不然拖拽第二个组件将会和第一个组件一模一样，改变第一个组件第二个、第三个也会跟着变动。
      const data = JSON.parse(JSON.stringify(origin))
      data.key = parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16);
      return data
    }
  }
}
</script>

<style scoped>
.custom-form-item-view {
  width: 400px;
  min-height: calc(100vh - 100px);
}

.widget-title {
  font-size: 16px;
  font-weight: 600;
}
.widget-group {
  display: flex;
  flex-wrap: wrap;
}
.widget-view {
  display: flex;
  align-items: center;
  width: 50%;
}
.widget {
  display: flex;
  align-items: center;
  margin: 5px;
  background: #f6f7ff;
  padding: 5px;
  width: 100%;
  cursor: all-scroll;
  border-radius: 5px;
}
</style>
