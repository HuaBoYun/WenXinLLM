<template>
  <div>
    <el-form label-width="100px" ref="elForm">
      <el-form-item label="授权角色" prop="operationData.rname">
        <el-input v-model="operationData.rname" disabled></el-input>
      </el-form-item>
      <el-form-item label="授权菜单" prop="operationData.rightNames">
        <el-input
          type="textarea"
          v-model="operationData.rightNames"
          disabled
        ></el-input>
        <el-button
          type="primary"
          @click="openMenu"
          v-if="primaryInfo.status == 2"
        >
          选择
        </el-button>
      </el-form-item>

      <el-form-item label="描述">
        <el-input
          type="textarea"
          v-model="primaryInfo.operationMemo"
          disabled
        ></el-input>
      </el-form-item>
    </el-form>

    <div class="footer" style="text-align: right" v-if="!disabled">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="save">确 定</el-button>
      <el-button @click="ymsubmit" type="primary">提交</el-button>
    </div>
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="formId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
    <MenuModal ref="menu" @menu-selection-change="handleMenuSelectionChange" />
  </div>
</template>

<script>
  import { grantRoleRight } from '@/api/setting/auth'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import MenuModal from './menuModal.vue'
  export default {
    name: 'MemuSQ',
    components: { Resubmit, MenuModal },
    data() {
      return {
        formId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        isWfqdedit: '',
        status: '',
        operationData: {},
        form: {
          roleId: '',
          rightIds: '',
          moduleType: 'xtsz',
        },
      }
    },

    methods: {
      showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        primaryInfo
      ) {
        this.disabled = title == 'detail'
        // this.getTreeselect()
        console.log(primaryInfo, 'primaryInfo')

        this.primaryInfo = primaryInfo
        this.operationData = JSON.parse(primaryInfo.operationData)
        this.formId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.isWfqdedit = isWfqdedit
        this.status = status

        // 初始化 form 数据
        this.form.roleId = this.primaryInfo.targetId
        this.form.rightIds = this.operationData.rightIds || ''
        this.form.moduleType = this.operationData.moduleType || 'xtsz'
      },
      close() {
        this.$refs['elForm'].resetFields()
        this.menuForm = this.$options.data().menuForm
        this.$bus.$emit('updateMsg', 0)
      },
      async save() {
        // 使用 form 数据而不是 menuForm
        const { msg, data } = await grantRoleRight(this.form)

        this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        this.$emit('fetch-data')
        // this.close()
      },
      async ymsubmit() {
        this.$refs.resubmit.ymsubmit()
      },
      openMenu() {
        this.$refs['menu'].showEdit(
          this.primaryInfo.targetId,
          this.operationData.rightIds,
          this.operationData.moduleType
        )
      },
      handleMenuSelectionChange(menuData) {
        // 更新操作数据中的权限ID和权限名称
        this.operationData.rightIds = menuData.rightIds
        this.operationData.rightNames = menuData.rightNames
        this.operationData.moduleType = menuData.moduleType

        // 更新表单数据
        this.form.roleId = this.primaryInfo.targetId
        this.form.rightIds = menuData.rightIds
        this.form.moduleType = menuData.moduleType

        console.log('菜单选择变化:', menuData)
        console.log('更新后的operationData:', this.operationData)
      },
    },
  }
</script>
<style scoped>
  .input-psword {
    -webkit-text-security: disc;
  }

  .compare-container {
    display: flex;
    gap: 20px;
    padding: 20px;
    position: relative;
    padding-bottom: 80px;
  }

  .compare-panel {
    flex: 1;
    border: 1px solid #e4e7ed;
    border-radius: 4px;
    background: #fff;
  }

  .panel-header {
    background: #f5f7fa;
    padding: 15px 20px;
    border-bottom: 1px solid #e4e7ed;
  }

  .panel-header h3 {
    margin: 0;
    color: #303133;
    font-size: 16px;
    font-weight: 500;
  }

  .compare-panel .el-form {
    padding: 20px;
  }

  .footer {
    /* position: absolute;
    bottom: 0;
    right: 0; */
    background: #fff;
    /* padding: 15px 20px; */
    text-align: center;
    z-index: 1000;
  }

  .color-red {
    color: #f56c6c;
    font-size: 12px;
    margin-top: 5px;
    display: block;
  }
</style>
