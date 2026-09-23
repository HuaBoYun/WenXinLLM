<template>
  <div class="custom-form-list-view">
    <draggable
      style="height: 100%"
      v-model="formList"
      @end="dragEnd"
      group="itxst"
      animation="300"
      :move="onMove"
    >
      <transition-group
        style="
          min-height: 100%;
          display: flex;
          flex-wrap: wrap;
          align-content: flex-start;
        "
      >
        <div
          v-for="item in formList"
          :key="item.key"
          :style="'width:' + item.width + '%'"
        >
          <div
            :class="
              item.key === activeKey ? 'list-item item-active' : 'list-item'
            "
            @click="changeActive(item)"
          >
            <div v-if="item.required" class="item-required">*</div>
            <div class="item-name">{{ item.name }}</div>
            <div class="item-widget">
              <el-input
                v-if="item.type === 'input'"
                v-model="item.value"
                :placeholder="item.placeholder"
              ></el-input>
              <el-input
                v-if="item.type === 'textarea'"
                type="textarea"
                v-model="item.value"
                :placeholder="item.placeholder"
              ></el-input>
              <el-input
                v-if="item.type === 'password'"
                type="password"
                v-model="item.value"
                :placeholder="item.placeholder"
              ></el-input>
              <el-input-number
                v-if="item.type === 'number'"
                v-model="item.value"
                :placeholder="item.placeholder"
              ></el-input-number>
              <img
                v-if="item.type === 'editor'"
                style="width: 100%"
                src="@/assets/ueditor.png"
              />
              <el-select
                v-if="item.type === 'select'"
                v-model="item.value"
                :placeholder="item.placeholder"
              >
                <el-option
                  v-for="item in getSelectData(item)"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
              <el-cascader
                v-if="item.type === 'cascader'"
                v-model="item.value"
                :options="getCascaderData(item)"
              ></el-cascader>
              <el-radio-group v-if="item.type === 'radio'" v-model="item.value">
                <el-radio
                  v-for="(item, radioIndex) in getRadioData(item)"
                  :key="radioIndex"
                  :label="item.id"
                >
                  {{ radio.label }}
                </el-radio>
                <el-radio label="1">选项1</el-radio>
                <el-radio label="2">选项2</el-radio>
              </el-radio-group>
              <el-checkbox-group
                v-if="item.type === 'checkbox'"
                v-model="item.value"
              >
                <el-checkbox
                  v-for="(item, checkboxIndex) in getRadioData(item)"
                  :key="checkboxIndex"
                  :label="item.id"
                >
                  {{ radio.label }}
                </el-checkbox>
                <el-checkbox label="1" :name="item.key">选项1</el-checkbox>
                <el-checkbox label="2" :name="item.key">选项2</el-checkbox>
              </el-checkbox-group>
              <el-switch
                v-if="item.type === 'switch'"
                v-model="item.value"
              ></el-switch>
              <el-slider
                v-if="item.type === 'slider'"
                v-model="item.value"
                :step="10"
              ></el-slider>
              <el-time-select
                v-if="item.type === 'time'"
                v-model="item.value"
                :placeholder="item.placeholder"
              ></el-time-select>
              <el-time-picker
                v-if="item.type === 'time-range'"
                is-range
                v-model="item.value"
                range-separator="至"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                placeholder="选择时间范围"
              ></el-time-picker>
              <el-date-picker
                v-if="item.type === 'date'"
                v-model="item.value"
                type="date"
                :placeholder="item.placeholder"
              ></el-date-picker>
              <el-date-picker
                v-if="item.type === 'date-range'"
                v-model="item.value"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              ></el-date-picker>
              <el-rate
                v-if="item.type === 'rate'"
                v-model="item.value"
              ></el-rate>
              <el-color-picker
                v-if="item.type === 'color'"
                v-model="item.value"
              ></el-color-picker>
              <el-upload
                v-if="item.type === 'upload'"
                class="upload-demo"
                action="https://jsonplaceholder.typicode.com/posts/"
              >
                <el-button size="small" type="primary">点击上传</el-button>
              </el-upload>
            </div>
          </div>
        </div>
      </transition-group>
    </draggable>
  </div>
</template>

<script>
  import draggable from 'vuedraggable'

  export default {
    name: 'CustomFormList',
    components: {
      draggable,
    },
    data() {
      return {
        formList: [],
        activeKey: '', // 选择key
      }
    },
    methods: {
      //右边往左边拖动时的事件
      dragEnd(e) {},
      //move回调方法
      onMove(e, originalEvent) {
        this.moveId = e.relatedContext.element.id
        return true
      },
      // 组件选择
      changeActive(item) {
        this.activeKey = item.key
        this.$bus.$emit('selectCustomerFormItem', item)
      },
      // 获取下拉数据
      getSelectData(item) {},
      // 获取级联数据
      getCascaderData() {},
      // 获取单选数据
      getRadioData() {},
    },
  }
</script>

<style scoped>
  .custom-form-list-view {
    background: #eff0f1;
    width: 100%;
    min-height: calc(100vh - 100px);
    padding: 20px;
  }

  .list-item {
    display: flex;
    align-items: center;
    padding: 10px;
  }

  .item-required {
    color: red;
    width: 10px;
  }

  .item-name {
    width: 80px;
  }

  .item-widget {
    width: 100%;
  }

  .item-active {
    background: #f6f7ff;
    border-radius: 5px;
  }

  .edui-editor {
    width: 100% !important;
  }

  .el-select,
  .el-cascader {
    width: 100%;
  }
</style>
