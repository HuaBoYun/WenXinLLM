<!--
 * @Date: 2022-04-18 11:40:51
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-18 13:04:52
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/TemplateDetailContent.vue
-->
<template>
  <el-row :gutter="15">
    <el-form ref="form" label-width="100px" :value="form">
      <el-col :lg="12" :md="12" :sm="24">
        <el-form-item label="范本编号" prop="contractno">
          {{ form.contractno }}
        </el-form-item>
      </el-col>
      <el-col :lg="12" :md="12" :sm="24">
        <el-form-item label="范本名称" prop="contractname">
          {{ form.contractname }}
        </el-form-item>
      </el-col>
      <el-col :lg="12" :md="12" :sm="24">
        <el-form-item label="所属类型" prop="contracttype">
          {{ form.contracttype }}
        </el-form-item>
      </el-col>
      <el-col :lg="12" :md="12" :sm="24">
        <el-form-item label="所属公司" prop="jborgName">
          {{ form.jborgName }}
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="范本使用说明">
          {{ form.momoconcat }}
        </el-form-item>
      </el-col>
      <el-col :span="24" :push="2">
        <!-- <el-row>
          <el-col :span="24">
            <el-row>
              <el-col :span="24">
                <UEditor v-model="form.describe" disabled :height="300" />
              </el-col>
            </el-row>
          </el-col>
        </el-row> -->
        <el-button
          :disabled="false"
          class="add-btn"
          size="mini"
          type="primary"
          @click="office()"
          v-if="!$store.state.work.processMobile"
        >
          预览范本
        </el-button>
        <!-- <el-button
          :disabled="!contractid"
          class="add-btn"
          size="mini"
          type="primary"
          @click="openWebOffice()"
          v-if="!$store.state.work.processMobile"
        >
          编辑范本内容
        </el-button> -->
      </el-col>
    </el-form>

    <TextEditor ref="webOffice" />
  </el-row>
</template>
<script>
  import UEditor from '@/components/UEditor'
  import TextEditor from './contractsEdit/TextEditor'
  export default {
    components: { UEditor, TextEditor },
    props: {
      formData: {
        type: Object,
        default: () => {},
      },
    },
    data() {
      return {
        form: this.formData,
        contractid: '',
      }
    },
    watch: {
      formData: {
        handler(newValue, oldValue) {
          if (newValue) {
            this.contractid = newValue.contractid
            this.form = newValue
          }
        },
        deep: true,
        immediate: true,
      },
    },
    methods: {
      //在线office
      openWebOffice() {
        //
        this.$store.commit('acl/contractidd', this.contractid)
        this.$refs['webOffice'].show()
      },
      //office处理
      office() {
        const info = JSON.parse(localStorage.getItem('userInfo'))
        window.open(
          `https://office.wenxin.example.com/api/office/getContractReview?fileType=word&contractId=${this.contractid}&uid=${info.staffid}&name=${info.username}`
        )
      },
    },
  }
</script>
