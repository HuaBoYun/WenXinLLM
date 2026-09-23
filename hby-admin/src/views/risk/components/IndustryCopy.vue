<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    :width="type === '1' ? '1000px' : '500px'"
    @close="close"
  >
    <div>
      <vab-query-form>
        <vab-query-form-top-panel>
          <el-form
            v-if="type === '1'"
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item class="">
              <el-input
                v-model="queryForm.flownumber"
                clearable
                placeholder="流程编号"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="queryForm.flowname"
                clearable
                placeholder="流程名称"
              />
            </el-form-item>
            <!-- <el-form-item>
              <el-cascader clearable :options="options" placeholder="行业" :props="{ checkStrictly: true }" />
            </el-form-item> -->
            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="fetchData"
              >
                查询
              </el-button>
            </el-form-item>
          </el-form>
          <vab-query-form-right-panel>
            <el-button type="success" v-if="type === '1'" @click="handleCopy">
              复制
            </el-button>
            <el-button type="success" v-if="type === '2'" @click="handleCopy">
              确定
            </el-button>
          </vab-query-form-right-panel>
        </vab-query-form-top-panel>
      </vab-query-form>
      <div class="lr-layout">
        <div class="left" :style="type === '1' ? '200px' : '300px'">
          <IndustryTree @fetch-data="getData" />
        </div>
        <div class="right" v-if="type === '1'">
          <el-table
            v-loading="listLoading"
            ref="multipleTable"
            :data="list"
            @select="handleSelectionChange"
          >
            <el-table-column type="selection" width="55" />
            <el-table-column
              align="center"
              label="流程编号"
              prop="flownumber"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleView(row, true)">
                  {{ row.flownumber }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="流程名称"
              prop="flowname"
              show-overflow-tooltip
            />
            <el-table-column align="center" label="机构" prop="data" />
            <el-table-column
              align="center"
              label="创建时间"
              prop="createtime"
            />
          </el-table>
          <el-pagination
            background
            :current-page="queryForm.pageNo"
            :layout="layout"
            :page-size="queryForm.pageSize"
            :total="total"
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
          />
        </div>
      </div>

      <RiskRead ref="read" />
    </div>
  </el-dialog>
</template>

<script>
  import { doEdit } from '@/api/table'
  import { listanalysishyCopy, toYwlcCopy, toYwlc } from '@/api/systemLog'
  import RiskRead from '@/views/risk/identify/creation/components/RiskRead.vue'
  export default {
    components: {
      IndustryTree: () => import('./IndustryTree.vue'),
      RiskRead,
    },

    name: 'IndustryCopy',
    data() {
      return {
        title: '从行业复制',
        dialogFormVisible: false,
        list: [],
        riskcatid: '',
        listLoading: true,
        current: {},
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        type: '',
        queryForm: {
          flownumber: '',
          flowname: '',
          moduletype: 'fxcj',
          pageNo: 1,
          pageSize: 20,
        },
        options: [
          {
            value: 'zhinan',
            label: '指南',
            children: [
              {
                value: 'shejiyuanze',
                label: '设计原则',
                children: [
                  {
                    value: 'yizhi',
                    label: '一致',
                  },
                  {
                    value: 'fankui',
                    label: '反馈',
                  },
                  {
                    value: 'xiaolv',
                    label: '效率',
                  },
                  {
                    value: 'kekong',
                    label: '可控',
                  },
                ],
              },
              {
                value: 'daohang',
                label: '导航',
                children: [
                  {
                    value: 'cexiangdaohang',
                    label: '侧向导航',
                  },
                  {
                    value: 'dingbudaohang',
                    label: '顶部导航',
                  },
                ],
              },
            ],
          },
          {
            value: 'zujian',
            label: '组件',
            children: [
              {
                value: 'basic',
                label: 'Basic',
                children: [
                  {
                    value: 'layout',
                    label: 'Layout 布局',
                  },
                  {
                    value: 'color',
                    label: 'Color 色彩',
                  },
                  {
                    value: 'typography',
                    label: 'Typography 字体',
                  },
                  {
                    value: 'icon',
                    label: 'Icon 图标',
                  },
                  {
                    value: 'button',
                    label: 'Button 按钮',
                  },
                ],
              },
              {
                value: 'form',
                label: 'Form',
                children: [
                  {
                    value: 'radio',
                    label: 'Radio 单选框',
                  },
                  {
                    value: 'checkbox',
                    label: 'Checkbox 多选框',
                  },
                  {
                    value: 'input',
                    label: 'Input 输入框',
                  },
                  {
                    value: 'input-number',
                    label: 'InputNumber 计数器',
                  },
                  {
                    value: 'select',
                    label: 'Select 选择器',
                  },
                  {
                    value: 'cascader',
                    label: 'Cascader 级联选择器',
                  },
                  {
                    value: 'switch',
                    label: 'Switch 开关',
                  },
                  {
                    value: 'slider',
                    label: 'Slider 滑块',
                  },
                  {
                    value: 'time-picker',
                    label: 'TimePicker 时间选择器',
                  },
                  {
                    value: 'date-picker',
                    label: 'DatePicker 日期选择器',
                  },
                  {
                    value: 'datetime-picker',
                    label: 'DateTimePicker 日期时间选择器',
                  },
                  {
                    value: 'upload',
                    label: 'Upload 上传',
                  },
                  {
                    value: 'rate',
                    label: 'Rate 评分',
                  },
                  {
                    value: 'form',
                    label: 'Form 表单',
                  },
                ],
              },
              {
                value: 'data',
                label: 'Data',
                children: [
                  {
                    value: 'table',
                    label: 'Table 表格',
                  },
                  {
                    value: 'tag',
                    label: 'Tag 标签',
                  },
                  {
                    value: 'progress',
                    label: 'Progress 进度条',
                  },
                  {
                    value: 'tree',
                    label: 'Tree 树形控件',
                  },
                  {
                    value: 'pagination',
                    label: 'Pagination 分页',
                  },
                  {
                    value: 'badge',
                    label: 'Badge 标记',
                  },
                ],
              },
              {
                value: 'notice',
                label: 'Notice',
                children: [
                  {
                    value: 'alert',
                    label: 'Alert 警告',
                  },
                  {
                    value: 'loading',
                    label: 'Loading 加载',
                  },
                  {
                    value: 'message',
                    label: 'Message 消息提示',
                  },
                  {
                    value: 'message-box',
                    label: 'MessageBox 弹框',
                  },
                  {
                    value: 'notification',
                    label: 'Notification 通知',
                  },
                ],
              },
              {
                value: 'navigation',
                label: 'Navigation',
                children: [
                  {
                    value: 'menu',
                    label: 'NavMenu 导航菜单',
                  },
                  {
                    value: 'tabs',
                    label: 'Tabs 标签页',
                  },
                  {
                    value: 'breadcrumb',
                    label: 'Breadcrumb 面包屑',
                  },
                  {
                    value: 'dropdown',
                    label: 'Dropdown 下拉菜单',
                  },
                  {
                    value: 'steps',
                    label: 'Steps 步骤条',
                  },
                ],
              },
              {
                value: 'others',
                label: 'Others',
                children: [
                  {
                    value: 'dialog',
                    label: 'Dialog 对话框',
                  },
                  {
                    value: 'tooltip',
                    label: 'Tooltip 文字提示',
                  },
                  {
                    value: 'popover',
                    label: 'Popover 弹出框',
                  },
                  {
                    value: 'card',
                    label: 'Card 卡片',
                  },
                  {
                    value: 'carousel',
                    label: 'Carousel 走马灯',
                  },
                  {
                    value: 'collapse',
                    label: 'Collapse 折叠面板',
                  },
                ],
              },
            ],
          },
          {
            value: 'ziyuan',
            label: '资源',
            children: [
              {
                value: 'axure',
                label: 'Axure Components',
              },
              {
                value: 'sketch',
                label: 'Sketch Templates',
              },
              {
                value: 'jiaohu',
                label: '组件交互文档',
              },
            ],
          },
        ],
      }
    },
    created() {},
    methods: {
      handleSelectionChange(val) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        console.log('val', val)
        this.current = val[0]
        console.log('this.current', this.current)
      },
      handleView(row) {
        this.$refs['read'].showRead(row)
      },
      handleCopy() {
        if (this.type === '1') {
          console.log('this.current', this.current)
          const data = {
            flowid: this.current.flowid,
            riskcatid: this.riskcatid,
          }
          console.log('data', data)
          toYwlcCopy(data).then((res) => {})
        }
      },
      showEdit(id, type) {
        this.type = type
        if (this.type === '1') {
          this.riskcatid = id
          this.title = '从行业复制'
        } else {
          this.title = '流程类型'
          this.riskcatid = id
        }
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
      async save() {
        const { msg } = await doEdit(this.form)
        this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        this.$emit('fetch-data')
        this.close()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },

      getData(id) {
        if (id) {
          this.queryForm.flowid = id
        } else {
          this.queryForm.flowid = 0
        }
        if (this.type === '1') {
          this.fetchData()
        }
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            page: { records, total },
          },
        } = await listanalysishyCopy(this.queryForm)
        this.list = records
        this.total = total
        this.listLoading = false
      },
    },
  }
</script>
<style lang="scss" scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
    height: 500px;
    overflow: hidden;
    overflow-y: auto;
  }

  .right {
    width: calc(100% - 200px);
  }

  #sign .el-table__header-wrapper .el-checkbox {
    display: none;
  }
</style>
