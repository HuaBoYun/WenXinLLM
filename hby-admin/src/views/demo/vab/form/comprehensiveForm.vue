<template>
  <div class="comprehensive-form-container">
    <el-row :gutter="20">
      <el-col
        :lg="{ span: 12, offset: 6 }"
        :md="{ span: 20, offset: 2 }"
        :sm="{ span: 20, offset: 2 }"
        :xl="{ span: 12, offset: 6 }"
        :xs="24"
      >
        <vab-query-form>
          <vab-query-form-left-panel>
            <el-radio-group v-model="labelPosition">
              <el-radio-button label="left">左对齐</el-radio-button>
              <el-radio-button label="right">右对齐</el-radio-button>
              <el-radio-button label="top">顶部对齐</el-radio-button>
            </el-radio-group>
          </vab-query-form-left-panel>
        </vab-query-form>
        <el-form
          ref="form"
          class="demo-form"
          :label-position="labelPosition"
          label-width="100px"
          :model="form"
          :rules="rules"
        >
          <el-form-item label="活动名称" prop="name">
            <el-input v-model="form.name" />
          </el-form-item>
          <el-form-item label="活动区域" prop="region">
            <el-select v-model="form.region" placeholder="请选择活动区域">
              <el-option label="区域一" value="shanghai" />
              <el-option label="区域二" value="beijing" />
            </el-select>
          </el-form-item>
          <el-form-item label="活动时间" prop="date">
            <el-date-picker
              v-model="form.date"
              placeholder="选择日期时间"
              type="datetime"
            />
          </el-form-item>
          <el-form-item label="即时配送" prop="delivery">
            <el-switch v-model="form.delivery" />
          </el-form-item>
          <el-form-item label="活动性质" prop="type">
            <el-checkbox-group v-model="form.type">
              <el-checkbox label="美食/餐厅线上活动" name="type" />
              <el-checkbox label="地推活动" name="type" />
              <el-checkbox label="线下主题活动" name="type" />
              <el-checkbox label="单纯品牌曝光" name="type" />
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="特殊资源" prop="resource">
            <el-radio-group v-model="form.resource">
              <el-radio label="线上品牌商赞助" />
              <el-radio label="线下场地免费" />
            </el-radio-group>
          </el-form-item>
          <el-form-item label="活动形式" prop="description">
            <el-input v-model="form.description" type="textarea" />
          </el-form-item>
          <el-form-item label="评星">
            <el-rate v-model="form.rate" show-text />
          </el-form-item>
          <el-form-item label="行政区划">
            <el-cascader
              v-model="form.area"
              clearable
              filterable
              :options="areaOptions"
              :props="{ label: 'name', value: 'code' }"
            />
          </el-form-item>
          <el-form-item label="穿梭框">
            <el-transfer
              v-model="form.transfer"
              :data="data"
              :filter-method="filterMethod"
              filter-placeholder="请输入城市拼音"
              filterable
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm('form')">
              立即创建
            </el-button>
            <el-button @click="resetForm('form')">重置</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import { getList } from '@/api/area'

  export default {
    name: 'ComprehensiveForm',
    data() {
      const generateData = () => {
        const data = []
        const cities = ['上海', '北京', '广州']
        const pinyin = ['shanghai', 'beijing', 'guangzhou']
        cities.forEach((city, index) => {
          data.push({
            label: city,
            key: index,
            pinyin: pinyin[index],
          })
        })
        return data
      }
      return {
        labelPosition: 'right',
        form: {
          name: '',
          region: '',
          date: '',
          date2: '',
          delivery: false,
          type: [],
          resource: '',
          description: '',
          rate: 0,
          area: [],
          transfer: [],
        },
        areaOptions: [],
        rules: {
          name: [
            { required: true, message: '请输入活动名称', trigger: 'blur' },
            {
              min: 3,
              max: 5,
              message: '长度在 3 到 5 个字符',
              trigger: 'blur',
            },
          ],
          region: [
            { required: true, message: '请选择活动区域', trigger: 'change' },
          ],
          date: [
            {
              type: 'date',
              required: true,
              message: '请选择日期',
              trigger: 'change',
            },
          ],
          type: [
            {
              type: 'array',
              required: true,
              message: '请至少选择一个活动性质',
              trigger: 'change',
            },
          ],
          resource: [
            { required: true, message: '请选择活动资源', trigger: 'change' },
          ],
          description: [
            { required: true, message: '请填写活动形式', trigger: 'blur' },
          ],
        },
        data: generateData(),
        filterMethod(query, item) {
          return item.pinyin.indexOf(query) > -1
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      //获取行政区划
      async fetchData() {
        const {
          data: { list },
        } = await getList()
        this.areaOptions = list
      },
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            alert('submit!')
          } else {
            // eslint-disable-next-line no-console
          }
        })
      },
      resetForm(formName) {
        this.$refs[formName].resetFields()
      },
    },
  }
</script>

<style lang="scss" scoped>
  .comprehensive-form-container {
    .demo-form {
      margin-top: 10px;
    }

    ::v-deep {
      .el-form-item__content {
        .el-rate {
          display: inline-block;
          font-size: 0;
          line-height: 1;
          vertical-align: middle;
        }

        .el-transfer__buttons {
          padding: 10px 10px 0 10px;
        }
      }
    }
  }
</style>
