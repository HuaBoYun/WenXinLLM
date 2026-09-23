<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <div>
      <el-tabs v-model="activeName" @tab-click="handleClick" stretch="true">
        <el-tab-pane label="基本信息" name="first">
          <el-form ref="form" label-width="160px" :model="form" :rules="rules">
            <el-row :gutter="30">
              <el-col :span="12">
                <el-form-item label="指标编号" prop="articletitle">
                  <el-input
                    v-model.trim="form.articletitle"
                    style="width: 300px"
                    :disabled="!footer"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="指标名称" prop="articletitle">
                  <el-input
                    v-model.trim="form.articletitle"
                    style="width: 300px"
                    :disabled="!footer"
                  />
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="单位类型" prop="articletitle">
                  <el-select
                    v-model="form.articlestatusText"
                    placeholder="请选择"
                    :disabled="!footer"
                  >
                    <el-option
                      v-for="(item, index) in statusList1"
                      :key="index"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="规则状态" prop="articletitle">
                  <el-select
                    v-model="form.articlestatusText"
                    placeholder="请选择"
                    :disabled="!footer"
                  >
                    <el-option
                      v-for="(item, index) in statusList2"
                      :key="index"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <!-- <el-col :span="12">
                <el-form-item label="数据连接字符串" prop="articlestatusText">
                  <el-select
                    v-model="form.articlestatusText"
                    placeholder="请选择"
                    :disabled="!footer"
                  >
                    <el-option
                      v-for="(item, index) in statusList3"
                      :key="index"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col> -->
              <el-col :span="24">
                <el-form-item label="指标所属部门" prop="articletitle">
                  <el-input
                    v-model.trim="form.articletitle"
                    style="width: 300px"
                    :disabled="!footer"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="创建人" prop="articletitle">
                  <el-input
                    v-model.trim="form.articletitle"
                    style="width: 300px"
                    :disabled="!footer"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="创建时间" prop="articletitle">
                  <el-input
                    v-model.trim="form.articletitle"
                    style="width: 300px"
                    :disabled="!footer"
                  />
                </el-form-item>
              </el-col>

              <el-col :span="24">
                <el-form-item label="指标描述：(最多300个汉字)	">
                  <el-input
                    v-model.trim="form.memo"
                    type="textarea"
                    rows="3"
                    :disabled="!footer"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="计算公式" name="second">
          <el-row>
            <el-col :span="24">
              <div class="but" v-if="footer">
                <el-button type="primary" @click="getDateSet()">
                  选择数据项
                </el-button>
                <el-button type="primary">验证规则</el-button>
              </div>
            </el-col>
            <el-form
              ref="form"
              label-width="160px"
              :model="form"
              :rules="rules"
            >
              <el-col :span="16">
                <el-form-item label="计算公式">
                  <el-input
                    v-model.trim="form.memo"
                    type="textarea"
                    rows="3"
                    :disabled="!footer"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <div class="view" v-if="footer">
                  <div class="view2">
                    <el-button size="mini">+</el-button>
                    <el-button size="mini">-</el-button>
                    <el-button size="mini">*</el-button>
                  </div>
                  <div>
                    <el-button size="mini">/</el-button>
                    <el-button size="mini">(</el-button>
                    <el-button size="mini">)</el-button>
                  </div>
                </div>
              </el-col>
              <el-col :span="16">
                <el-form-item label="计算公式描述">
                  <el-input
                    v-model.trim="form.memo"
                    type="textarea"
                    rows="3"
                    :disabled="!footer"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <div class="view" v-if="footer">
                  <div class="view2">
                    <el-button size="mini">1</el-button>
                    <el-button size="mini">2</el-button>
                    <el-button size="mini">3</el-button>
                    <el-button size="mini">4</el-button>
                    <el-button size="mini">5</el-button>
                    <el-button size="mini">6</el-button>
                  </div>
                  <div>
                    <el-button size="mini">7</el-button>
                    <el-button size="mini">8</el-button>
                    <el-button size="mini">9</el-button>
                    <el-button size="mini">0</el-button>
                    <el-button size="mini">%</el-button>
                    <el-button size="mini">.</el-button>
                  </div>
                </div>
              </el-col>
            </el-form>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="指标基准阈值" name="third">
          <el-row>
            <el-col :span="24">
              <el-divider>指标值单位：比率%</el-divider>
            </el-col>
            <el-col :span="24">
              <vab-query-form>
                <vab-query-form-left-panel></vab-query-form-left-panel>
                <vab-query-form-right-panel>
                  <el-button
                    type="success"
                    @click="handleDataAdd"
                    v-if="footer"
                  >
                    增加
                  </el-button>
                </vab-query-form-right-panel>
              </vab-query-form>

              <el-table v-loading="listLoading" :data="list">
                <el-table-column
                  align="center"
                  label="序号"
                  prop="aruticleauther"
                />
                <el-table-column
                  align="center"
                  label="容忍度"
                  prop="publishtime"
                />
                <el-table-column
                  align="center"
                  label="指标阈值名称"
                  prop="publishtime"
                />
                <el-table-column
                  align="center"
                  label="容忍度下边界"
                  prop="aruticleauther"
                />
                <el-table-column
                  align="center"
                  label="容忍度上边界"
                  prop="publishtime"
                />
                <el-table-column
                  align="center"
                  label="门槛区间值"
                  prop="publishtime"
                />
                <el-table-column
                  align="center"
                  label="指标预警方式"
                  prop="publishtime"
                />
                <el-table-column
                  align="center"
                  label="操作"
                  show-overflow-tooltip
                  width="120"
                >
                  <template #default="{ row }" v-if="footer">
                    <el-button type="text" @click="handleDelete(row)">
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-pagination
                background
                :current-page="queryForm.pageNumber"
                :layout="layout"
                :page-size="queryForm.pageSize"
                :total="total"
                @current-change="handleCurrentChange"
                @size-change="handleSizeChange"
              />
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </div>

    <template #footer v-if="footer">
      <el-button @click="close">取 消</el-button>
      <el-button @click="rest">重 置</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
    <!-- <DataSet ref="dataSet" /> -->
  </el-dialog>
</template>

<script>
  import {
    mergeOtherarticle,
    selectOtherarticleInfo,
  } from '@/api/workbench/auditTools'
  // import DataSet from './DataSet.vue'
  export default {
    // components: { DataSet },
    name: 'LcdyEdit',

    data() {
      return {
        baseApi:
          process.env.NODE_ENV === 'development'
            ? '/vab-mock-server/audit'
            : process.env.VUE_APP_BASE_API,
        api: '/fileManage/upload',
        form: {
          articlebody: '',
          articlestatus: '',
          articlestatusText: '',
          articletitle: '',
          aruticleauther: '',
          memo: '',
          publishtime: '',
          attIds: [],
        },
        statusList1: [
          { label: '人民币', value: '高' },
          { label: '美元', value: '中' },
          { label: '时间', value: '低' },
          { label: '比率', value: '低' },
          { label: '其他', value: '低' },
        ],
        statusList2: [
          { label: '正常', value: '0' },
          { label: '禁用', value: '1' },
        ],
        statusList3: [
          { label: '账簿1', value: '0' },
          { label: '账簿2', value: '1' },
        ],
        // disabled: false,
        templates: [],
        fileList: [],
        // tableData: [],
        rules: {
          articletitle: [
            { required: true, trigger: 'blur', message: '请输入文章标题' },
          ],
          aruticleauther: [
            { required: true, trigger: 'blur', message: '请输入文章作者' },
          ],
          articlestatusText: [
            { required: true, trigger: 'blur', message: '请选择状态' },
          ],
          publishtime: [
            { required: true, trigger: 'blur', message: '请输入发布时间' },
          ],
        },
        title: '',
        dialogFormVisible: false,
        footer: false,
        options: [],
        activeName: 'first',
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          articletitle: '',
          aruticleauther: '',
          orgid: '',
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      handleClick(tab, event) {},
      getDateSet() {
        this.$refs['dataSet'].show()
      },
      async showEdit(row, flag) {
        if (flag == 'add') {
          this.title = '新增'
          this.footer = true
        } else if (flag == 'edit') {
          this.title = '编辑'
          this.footer = true
        } else {
          this.title = '详情'
          this.footer = false
        }
        // if (row) {
        //   this.form = row
        // }
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.disabled = false
        this.dialogFormVisible = false
      },
      handleDepartmentSelected(node) {
        const data = node.id ? node : node.checked
        this.form.orgid = data.id
        this.form.orgname = data.text
      },
      save() {
        this.form.articlestatus =
          this.statusList[this.form.articlestatusText].label
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.form.publishtime = this.form.publishtime.substring(0, 11)
            const { msg } = await mergeOtherarticle(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        // this.listLoading = true
        // const {
        //   data: {
        //     pageInfo: { tlist, totalRecord },
        //   },
        // } = await getOtherarticlePageList(this.queryForm)
        // this.list = tlist
        // this.total = totalRecord
        this.listLoading = false
      },
    },
  }
</script>
<style scoped>
  /* ::v-deep .el-tabs__nav-scroll {
    width: 50%;
    margin: 0 auto;
  } */

  /* ::v-deep .el-tabs__nav {
    width: 100%;
  } */
  .but {
    text-align: center;
    margin-bottom: 20px;
  }
  .view {
    text-align: center;
  }
  .view2 {
    margin-bottom: 10px;
  }
</style>
