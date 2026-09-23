<template>
  <div>
    <el-row>
      <el-form
        ref="ruleForm"
        label-width="140px"
        :model="formData"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12">
          <el-form-item label="编号" prop="hzname">
            <el-input
              v-model="formData.hzname"
              clearable
              placeholder="请输入编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报单位" prop="orgname">
            <el-input
              v-model="formData.orgname"
              placeholder="请选择填报单位"
              :style="{ width: '78%' }"
              readonly
              disabled
            />
            <el-button
              type="primary"
              size="mini"
              style="margin-left: 15px"
              @click="$refs.audiTree.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目类别" prop="fl">
            <el-select style="width: 100%" v-model="formData.fl" disabled>
              <el-option label="三类" value="三类"></el-option>
              <el-option label="四类" value="四类"></el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="备注" prop="hzbz">
            <el-input
              v-model="formData.hzbz"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="6"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="cjr">
            <el-input
              v-model="formData.cjr"
              disabled
              placeholder="请输入创建人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="cjsj">
            <el-date-picker
              v-model="formData.cjsj"
              placeholder="请输入创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24" class="flex-right" v-if="!formDisabled">
          <el-button type="success" @click="handleEdit(null)">新增</el-button>
          <!-- <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="handleEdit(null, '三类')">
                三类
              </el-dropdown-item>
              <el-dropdown-item @click.native="handleEdit(null, '四类')">
                四类
              </el-dropdown-item>
            </el-dropdown-menu> -->
        </el-col>
        <el-table :data="list">
          <el-table-column align="center" label="序号" type="index" />
          <el-table-column align="center" label="合同编号" prop="htbh">
            <template #default="{ row }">
              <el-button
                :disabled="false"
                type="text"
                @click="handleDetail(row)"
              >
                {{ row.htbh }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column align="center" label="项目类别" prop="fl" />
          <el-table-column
            align="center"
            label="验收项目名称"
            prop="ysxmmc"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="填报单位"
            prop="tborgname"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="计划文号"
            prop="jhwh"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="计划投资金额（万元）"
            prop="jhtzje"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="承包方式（EPC或施工承包）"
            prop="cbfs"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="开竣工时间"
            prop="kjgsj"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="项目投产时间"
            prop="xmtcsj"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="试运行合格时间"
            prop="syxhgsj"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="其他专项验收情况"
            prop="qtzxysqk"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="资料归档时间"
            prop="zlgdsj"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="完成工程结算时间（二审）"
            prop="wcgcjssj"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="工程费用结算金额"
            prop="gcfyjsje"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="是否完成财务决算"
            prop="sfwccwjs"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="拟竣工验收时间"
            prop="njgyssj"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="项目负责人"
            prop="xmfzr"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="联系电话"
            prop="lxdh"
            show-overflow-tooltip
          />
          <el-table-column
            label="操作"
            fixed="right"
            align="center"
            width="160"
            v-if="!formDisabled"
          >
            <template #default="{ row, $index }">
              <el-button
                type="text"
                @click="handleEdit(row)"
                :disabled="formDisabled"
              >
                修改
              </el-button>
              <el-button
                type="text"
                :disabled="formDisabled"
                @click.native="handleDelete(row, $index)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div style="text-align: right; margin-top: 20px" v-if="!formDisabled">
        <el-button @click="close">取消</el-button>
        <el-button @click="submit" type="primary">确定</el-button>
        <el-button type="primary" @click="ymsubmit" :disabled="btnLoading">
          提交
        </el-button>
      </div>
      <jsxmjbqkEdit ref="edit" @fetch-data="fetchData" />
    </el-row>
    <Resubmit
      ref="resubmit"
      @fetchClose="close"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      :status="status"
    />
    <SelectDepartment ref="audiTree" @submit="handleUnitSelected" />
  </div>
</template>
<script>
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import jsxmjbqkEdit from '@/views/oilAudit/gcgl/components/jsxmjbqkEdit.vue'
  import {
    flVerify,
    jsxmjbqkHzSaveOrUpdate,
    jsxmjbqkHzDetail,
  } from '@/oapi/audit/plan'
  export default {
    components: { Resubmit, jsxmjbqkEdit, SelectDepartment },

    data() {
      return {
        formDisabled: true,
        formData: {
          cjr: '',
          cjsj: '',
          fl: '',
          hzbz: '',
          hzname: '',
          hzid: '',
          orgid: '',
          orgname: '',
        },
        list: [],
        // 流程相关
        flowtaskinfoflowid: '',
        fromId: '',
        ymFromId: '',
        fromIdcopy: '',
        status: '',
        jsxmtzwcqkid: '',
        btnLoading: false,
      }
    },
    mounted() {},
    methods: {
      async ymsubmit() {
        try {
          this.$refs['ruleForm'].validate(async (valid) => {
            if (valid) {
              this.btnLoading = true
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
      async showEdit(
        title,
        row,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        // 流程相关
        this.fromId = row
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        if (title == 'edit') {
          this.formDisabled = false
        } else if (title == 'detail') {
          this.formDisabled = true
        }
        this.getDetail(row)
      },
      async getDetail(row) {
        // 获取数据
        const { data } = await jsxmjbqkHzDetail({
          hzid: row,
        })
        console.log(data, 'data')
        // Object.assign(this.formData, data)
        Object.keys(this.formData).forEach(
          (key) => (this.formData[key] = data[key])
        )
        this.list = data.listJsxmJbqk || []
      },
      async handleDetail(row) {
        await this.$refs['edit'].showEdit(row, true)
      },
      async handleEdit(row, fl) {
        if (!row && fl === '四类') {
          const flVerifyRes = await flVerify({ fl })
          if (!flVerifyRes || !flVerifyRes.data) {
            return
          }
        }
        this.$refs['edit'].showEdit(row, false, this.formData.fl)
      },
      async handleDelete(row, index) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          this.$message.success('操作成功！')
          this.list.splice(index, 1)
        })
      },

      fetchData(row, type) {
        // row 新建 1 提交后返回值
        if (type == 'add') {
          this.list.push(row)
        } else {
          this.list.map((v, i) => {
            if (v.jsxmjbqkid == row.jsxmjbqkid) {
              this.$set(this.list, i, row)
            }
            return v
          })
        }
      },
      async submit() {
        let ids = this.list
          .map((v) => {
            return v.jsxmjbqkid
          })
          .toString()
        let params = { ...this.formData, jsxmjbqkids: ids }
        delete params.cjr
        delete params.cjsj
        delete params.listJsxmJbqk
        const { data, msg, result } = await jsxmjbqkHzSaveOrUpdate(params)
        if (result == 500) return this.$message.error(msg)
        this.$message.success(msg)
        this.$emit('fetchData')
      },
      close() {
        this.tableData = []
        this.formDisabled = true
        this.$bus.$emit('updateMsg', 0)
      },
      /**
       * @description: 选择单位
       * @param {*}
       * @return {*}
       */
      handleUnitSelected(node) {
        this.formData.orgname = node.label
        this.formData.orgid = node.id
      },
    },
  }
</script>
<style lang="scss" scoped>
  .flex-right {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 10px;
  }
</style>
