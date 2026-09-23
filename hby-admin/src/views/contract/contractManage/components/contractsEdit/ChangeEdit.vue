<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="15" v-loading="showLoading">
      <el-form ref="form" label-width="150px" :model="formData" :rules="rules">
        <el-col :span="24">
          <el-divider>合同变更内容</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同变更类型" prop="changetype">
            <el-select
              v-model="formData.changetype"
              filterable
              :disabled="!allDisable"
              placeholder="请选择合同变更类型"
              style="width: 100%"
            >
              <el-option
                v-for="item in changeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.label"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同变更时间" prop="changedate">
            <el-date-picker
              :disabled="!allDisable"
              v-model="formData.changedate"
              clearable
              placeholder="请输入合同变更时间"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="合同变更内容" prop="changedesc">
            <el-input
              :disabled="!allDisable"
              v-model="formData.changedesc"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入合同变更内容"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>合同基本信息</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号" prop="contractno">
            <el-input
              v-model="formData.contractno"
              clearable
              placeholder="请输入合同编号"
              disabled
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item ref="contractname" label="合同名称" prop="contractname">
            <el-input
              v-model="formData.contractname"
              clearable
              :disabled="formFields['contractname'].disabled"
              placeholder="请输入合同名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="项目编号" prop="contractitem">
            <el-input
              v-model="formData.contractitem"
              clearable
              :disabled="formFields['contractitem'].disabled"
              placeholder="请输入项目编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="项目名称" prop="topicid">
            <el-input
              v-model="formData.topicname"
              disabled
              placeholder="请选择项目名称"
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="formFields['topicid'].disabled"
              @click="$refs.setup.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同类型" prop="contracttype">
            <el-input
              v-model="formData.contracttype"
              :disabled="formFields['contracttype'].disabled"
              placeholder=""
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="formFields['startdate'].label" prop="startdate">
            <el-date-picker
              v-model="formData.startdate"
              clearable
              :disabled="
                formFields['startdate'].disabled &&
                formFields['momoconcat'].disabled
              "
              :placeholder="`请输入${formFields['startdate'].label}`"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="formFields['enddate'].label" prop="enddate">
            <el-date-picker
              v-model="formData.enddate"
              clearable
              :disabled="
                formFields['enddate'].disabled &&
                formFields['momoconcat'].disabled
              "
              :placeholder="`请输入${formFields['enddate'].label}`"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="事项审议机构" prop="matterorg">
            <el-select
              v-model="formData.matterorg"
              :disabled="formFields['matterorg'].disabled"
              placeholder="请选择"
              :style="{ width: '100%', height: '28px' }"
            >
              <el-option key="董事会" label="董事会" value="董事会"></el-option>
              <el-option key="经营层" label="经营层" value="经营层"></el-option>
              <el-option
                key="公司党委"
                label="公司党委"
                value="公司党委"
              ></el-option>
              <el-option
                key="公司工会"
                label="公司工会"
                value="公司工会"
              ></el-option>
              <el-option key="其他" label="其他" value="其他"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="formData.matterorg == '董事会'">
          <el-form-item label="是否三重一大事项" prop="isbigmatter">
            <template>
              <el-radio-group
                v-model="formData.isbigmatter"
                :disabled="formFields['isbigmatter'].disabled"
              >
                <el-radio label="是">是</el-radio>
                <el-radio label="否">否</el-radio>
              </el-radio-group>
            </template>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item
            :label="formFields['budgetname'].label"
            prop="budgetname"
          >
            <el-input
              v-model="formData.budgetname"
              readonly
              :placeholder="`请选择${formFields['budgetname'].label}`"
              :style="{ width: '256px' }"
            />
            <el-button
              :disabled="formFields['budgetname'].disabled"
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.xdf.show('HTGL001', 'budgetname')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
        <el-col v-if="formFields['counterparttype']" :span="12">
          <el-form-item
            :label="formFields['counterparttype'].label"
            prop="counterparttype"
          >
            <el-input
              v-model="formData.counterparttype"
              placeholder=""
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col v-if="formFields['director']" :span="12">
          <el-form-item :label="formFields['director'].label" prop="director">
            <el-input
              v-model="formData.director"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col v-if="formFields['counterpartaddress']" :span="12">
          <el-form-item
            :label="formFields['counterpartaddress'].label"
            prop="counterpartaddress"
          >
            <el-input
              v-model="formData.counterpartaddress"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col v-if="formFields['contacts']" :span="12" style="height: 50.5px">
          <el-form-item :label="formFields['contacts'].label" prop="contacts">
            <el-input
              v-model="formData.contacts"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col
          v-if="formFields['contactsphone']"
          :span="12"
          style="height: 50.5px"
        >
          <el-form-item
            :label="formFields['contactsphone'].label"
            prop="contactsphone"
          >
            <el-input
              v-model="formData.contactsphone"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col
          v-if="formFields['contractbd']"
          :span="12"
          style="height: 50.5px"
        >
          <el-form-item
            :label="formFields['contractbd'].label"
            prop="contractbd"
          >
            <el-input
              v-model="formData.contractbd"
              :disabled="formFields['contractbd'].disabled"
              :placeholder="`请输入${formFields['contractbd'].label}`"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col v-if="formFields['contractzd']" :span="12" style="height: 50.5px">
          <el-form-item :label="formFields['contractzd'].label" prop="contractzd">
            <template v-if="currentEdit == 'moren'">
              <el-select v-model="formData.contractzd" placeholder="请选择合同层级">
                <el-option
                  v-for="item in contractLevelOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </template>
            <template v-else>
              <el-input
                v-model="formData.contractzd"
                clearable
                :disabled="formFields['contractzd'].disabled"
                :placeholder="`请输入${formFields['contractzd'].label}`"
                :style="{ width: '100%' }"
              />
            </template>
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="12">
          <el-form-item label="银行账号" prop="bankaccount">
            <el-input
              v-model="formData.bankaccount"
              readonly
              placeholder="请选择银行账号"
              :style="{ width: '256px' }"
            />
            <el-button
              :disabled="formFields['bankaccount'].disabled"
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="handleChoseBankClick"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开户银行" prop="bankkhyh">
            <el-input
              v-model="formData.bankkhyh"
              placeholder="请输入开户银行"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col> -->
        <el-col
          v-if="formFields['contractdatetype']"
          :span="12"
          style="height: 50.5px"
        >
          <el-form-item
            :label="formFields['contractdatetype'].label"
            prop="contractdatetype"
          >
            <template v-if="currentEdit == 'moren'">
              <el-radio-group
                v-model="formData.contractdatetype"
                :disabled="formFields['contractdatetype'].disabled"
              >
                <el-radio label="固定期限">固定期限</el-radio>
                <el-radio label="无固定期限">无固定期限</el-radio>
              </el-radio-group>
            </template>
            <template v-else>
              <el-input
                v-model="formData.contractdatetype"
                clearable
                :disabled="formFields['contractdatetype'].disabled"
                :placeholder="`请输入${formFields['contractdatetype'].label}`"
                :style="{ width: '100%' }"
              />
            </template>
          </el-form-item>
        </el-col>
        <el-col
          v-if="formFields['contractplan']"
          :span="12"
          style="height: 50.5px"
        >
          <el-form-item
            :label="formFields['contractplan'].label"
            prop="contractplan"
          >
            <el-radio-group
              v-model="formData.contractplan"
              :disabled="formFields['contractplan'].disabled"
            >
              <el-radio label="是">是</el-radio>
              <el-radio label="否">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col
          v-if="formFields['contractxz']"
          :span="12"
          style="height: 50.5px"
        >
          <el-form-item
            :label="formFields['contractxz'].label"
            prop="contractxz"
          >
            <template v-if="currentEdit == 'moren'">
              <el-radio-group
                v-model="formData.contractxz"
                :disabled="formFields['contractxz'].disabled"
              >
                <el-radio label="初始合同">初始合同</el-radio>
                <el-radio label="补充合同">补充合同</el-radio>
              </el-radio-group>
            </template>
            <template v-else>
              <el-input
                v-model="formData.contractxz"
                clearable
                :disabled="formFields['contractxz'].disabled"
                :placeholder="`请输入${formFields['contractxz'].label}`"
                :style="{ width: '100%' }"
              />
            </template>
          </el-form-item>
        </el-col>
        <el-col v-if="formFields['contractlink']" :span="12">
          <el-form-item
            :label="formFields['contractlink'].label"
            prop="contractlink"
          >
            <el-input
              v-model="formData.contractlink"
              :disabled="formFields['contractlink'].disabled"
              :placeholder="`请输入${formFields['contractlink'].label}`"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <!-- jijiatype字段在金额信息部分也有用，在非默认模板情况下才用在合同基本信息 -->
        <el-col
          v-if="currentEdit !== 'moren' && formFields['jijiatype']"
          :span="12"
        >
          <el-form-item :label="formFields['jijiatype'].label" prop="jijiatype">
            <el-input
              v-model="formData.jijiatype"
              :disabled="formFields['jijiatype'].disabled"
              :placeholder="`请输入${formFields['jijiatype'].label}`"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 50.5px">
          <el-form-item label="我方签署主体" prop="orgname">
            <el-input
              v-model="formData.orgname"
              disabled
              placeholder="请选择我方签署主体"
              :style="{ width: '256px' }"
            />
            <!-- <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.unit.show()"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 50.5px">
          <el-form-item label="承办部门" prop="orgmeno">
            <el-input
              v-model="formData.orgmeno"
              disabled
              placeholder="请选择执行部门"
              :style="{ width: '256px' }"
            />
            <!-- <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.department.show()"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 50.5px">
          <el-form-item label="承办人" prop="realname">
            <el-input
              v-model="formData.realname"
              disabled
              placeholder="请选择承办人"
              :style="{ width: '256px' }"
            />
            <!-- <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.executor.show()"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 50.5px">
          <el-form-item label="授权委托人" prop="entrustStaffName">
            <el-input
              v-model="formData.entrustStaffName"
              clearable
              disabled
              placeholder="请选择授权委托人"
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="formFields['entrustStaffName'].disabled"
              @click="executorShow('entrustStaff')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <!-- <el-col v-if="currentEdit !== 'moren'" :span="12"> -->
        <!-- <el-col :span="12">
          <el-form-item label="项目名称" prop="topicid">
            <el-input
              v-model="formData.topicid"
              readonly
              placeholder="请选择项目名称"
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.setup.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
        <template v-if="formFields['contractchildren']">
          <el-col :span="12" style="height: 50.5px">
            <el-form-item label="是否关联合同" prop="contractchildren">
              <el-radio-group
                v-model="formData.contractchildren"
                :disabled="formFields['contractchildren'].disabled"
              >
                <el-radio label="是">是</el-radio>
                <el-radio label="否">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col v-show="formData['contractchildren'] == '是'" :span="12">
            <el-form-item label="关联合同" prop="parentname">
              <el-input
                v-model="formData.parentname"
                clearable
                disabled
                placeholder="请选择关联合同"
                :style="{ width: '196px' }"
              />
              <el-button
                v-if="formData.recordparent"
                :style="{ marginLeft: '10px' }"
                type="primary"
                :disabled="false"
                @click="openDetailRecord(formData.recordparent)"
              >
                详细
              </el-button>
              <el-button
                :style="{ marginLeft: '70px' }"
                type="primary"
                :disabled="formFields['parentname'].disabled"
                @click="$refs.xdf.show('HTGL005', 'parentname')"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
        </template>
        <el-col :span="24">
          <el-form-item
            :label="formFields['momoconcat'].label"
            prop="momoconcat"
          >
            <el-input
              v-model="formData.momoconcat"
              :autosize="{ minRows: 4, maxRows: 4 }"
              :disabled="formFields['momoconcat'].disabled"
              :placeholder="`请输入${formFields['momoconcat'].label}`"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col v-if="formFields['riskcontrol']" :span="24">
          <el-form-item :label="formFields['riskcontrol'].label">
            <el-input
              v-model="formData.riskcontrol"
              :autosize="{ minRows: 4, maxRows: 4 }"
              :disabled="formFields['riskcontrol'].disabled"
              :placeholder="`请输入${formFields['riskcontrol'].label}`"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>合同金额信息</el-divider>
        </el-col>
        <el-col :span="12" style="height: 50.5px">
          <el-form-item label="收付方向" prop="dctype">
            <el-radio-group
              v-model="formData.dctype"
              :disabled="formFields['dctype'].disabled"
              @change="dctypeChange"
            >
              <el-radio label="收款">收款</el-radio>
              <el-radio label="付款">付款</el-radio>
              <el-radio label="无">无</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 50.5px">
          <el-form-item
            :label="formFields['contractmoney'].label"
            prop="contractmoney"
          >
            <el-input
              v-model="formData.contractmoney"
              clearable
              :disabled="formFields['contractmoney'].disabled"
              :placeholder="`请输入${formFields['contractmoney'].label}`"
              :style="{ width: '100%' }"
              type="number"
              @blur="handleBlur"
            />
          </el-form-item>
        </el-col>
        <el-col v-if="currentEdit == 'moren'" :span="12" style="height: 50.5px">
          <el-form-item label="计价方式" prop="jijiatype">
            <el-radio-group
              v-model="formData.jijiatype"
              :disabled="formFields['jijiatype'].disabled"
              @change="jijiatypeChange"
            >
              <el-radio label="固定总价">固定总价</el-radio>
              <el-radio label="非固定总价">非固定总价</el-radio>
              <el-radio label="无金额">无金额</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 50.5px">
          <el-form-item label="币种" prop="moneytype">
            <el-input
              v-model="formData.moneytype"
              clearable
              :disabled="formFields['moneytype'].disabled"
              placeholder="请输入币种"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 50.5px">
          <el-form-item label="人民币大写" prop="hzsumowing">
            <el-input
              v-model="formData.hzsumowing"
              clearable
              disabled
              placeholder=""
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col v-if="showSubFields" :span="24">
          <el-divider>{{ subTitle }}</el-divider>
          <SubList
            :readonly="readonly"
            :contract="formData"
            :fields="subFields"
            @data-change="formData.informationList = $event"
          />
        </el-col>
        <el-col>
          <el-divider>相对方信息</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button
              type="primary"
              @click="handleAdds"
              :disabled="formFields['budgetname'].disabled"
            >
              增加一行
            </el-button>
          </div>
          <!-- 新增可编辑表格 -->
          <el-table :data="tableDataProject">
            <el-table-column align="center" label="相对方类型" prop="bugetType">
              <template slot-scope="scope">
                <el-select
                  placeholder="请选择"
                  v-model="scope.row.bugetType"
                  :disabled="formFields['budgetname'].disabled"
                >
                  <el-option
                    v-for="item in SSoptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="相对方名称"
              prop="budgetname"
              width="400px"
            >
              <template slot-scope="scope">
                <div style="margin-left: 40px; display: flex">
                  <el-input
                    disabled
                    v-model="scope.row.budgetname"
                    style="width: 60%; margin-left: 40px"
                  />
                  <el-button
                    :style="{ marginLeft: '10px' }"
                    type="primary"
                    :disabled="formFields['budgetname'].disabled"
                    @click="
                      $refs.xdfINfo.show('HTGL001', 'budgetname', scope.$index)
                    "
                  >
                    选择
                  </el-button>
                </div>
              </template>
            </el-table-column>

            <el-table-column align="center" label="操作">
              <template slot-scope="scope">
                <el-button
                  type="text"
                  @click="handleDelete(scope.$index)"
                  :disabled="formFields['budgetname'].disabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <!-- <el-col>
          <el-divider>关联OA公文/协同列表</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="success" @click="openOA()">新增</el-button>
          </div>
          <el-table :data="tableDataOA">
            <el-table-column align="center" label="标题" prop="subject" />
            <el-table-column align="center" label="类别" prop="cateGoryName">
              <template slot-scope="scope">
                <span v-if="scope.row.cateGoryName == 'edoc'">公文</span>
                <span v-if="scope.row.cateGoryName == 'col'">协同</span>
              </template>
            </el-table-column>
            <el-table-column align="center" label="类型" prop="stateName">
              <template slot-scope="scope">
                <span v-if="scope.row.stateName == '19'">发文</span>
                <span v-if="scope.row.stateName == '20'">收文</span>
                <span v-if="scope.row.stateName == '21'">签报</span>
              </template>
            </el-table-column>
            <el-table-column align="center" label="发送人" prop="sendName" />
            <el-table-column align="center" label="操作">
              <template slot-scope="scope">
                <el-button type="text" @click="handleDetail(scope.row)">
                  详细
                </el-button>
                <el-button type="text" @click="handleOADelete(scope.row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col> -->

        <el-col v-if="showPlanList" :span="24">
          <el-divider>合同阶段信息</el-divider>
          <JieduanList
            v-if="Jie"
            :readonly="formFields['orgname'].disabled"
            :contract="formData"
            @data-change="formData.nodeList = $event"
            ref="jieduanListRef"
            :XDFList="this.JDDetailXDFInfo"
          />
        </el-col>

        <el-col :span="24">
          <el-form-item label-width="0" prop="describe">
            <!-- <UEditor
              v-if="true || (showEditor && !beenClose)"
              v-model="formData.describe"
              :height="300"
              :templates="templates"
            /> -->

            <!-- <el-button
              :disabled="!contractid"
              class="add-btn"
              size="mini"
              type="primary"
              @click="openWebOffice()"
            >
              编辑合同内容
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col>
          <el-divider>合同正文</el-divider>
        </el-col>
        <el-col :span="3" v-if="!allDisable">
          <el-button
            class="add-btn"
            size="mini"
            type="primary"
            @click="handleoffice()"
            :disabled="false"
          >
            预览合同正文
          </el-button>
        </el-col>
        <el-col :span="3" v-if="allDisable">
          <el-button
            class="add-btn"
            size="mini"
            type="primary"
            :disabled="formFields['momoconcat'].disabled"
            @click="openOffice()"
          >
            编辑合同正文
          </el-button>
        </el-col>
        <el-col :span="3" v-if="allDisable">
          <el-upload
            ref="upload"
            accept=".doc,.docx"
            :show-file-list="false"
            :action="uploadUrl"
            :on-success="handleSuccess1"
            :before-upload="handleFileBefore"
            name="uploadedFile"
            :disabled="!contractid"
          >
            <el-button
              class="add-btn"
              size="mini"
              type="primary"
              :disabled="formFields['momoconcat'].disabled"
            >
              上传本地文件
            </el-button>
          </el-upload>
        </el-col>
        <el-col :span="3" v-if="allDisable">
          <el-button
            :disabled="formFields['momoconcat'].disabled"
            class="add-btn"
            size="mini"
            type="primary"
            @click="openTemplate()"
          >
            选择合同范本
          </el-button>
        </el-col>
        <el-col :span="24">
          <el-divider>附件</el-divider>
        </el-col>
        <el-col :span="24">
          <el-upload
            class="upload-demo"
            :show-file-list="false"
            :action="baseApi + api + '?contractId=' + contractid"
            :headers="headers"
            :on-preview="handlePreview"
            :on-success="handleSuccess"
            :file-list="pdfList"
            :before-upload="handleBeforeUpload"
            style="text-align: right"
          >
            <el-button
              type="primary"
              :disabled="formFields['momoconcat'].disabled"
            >
              上传
            </el-button>
          </el-upload>
        </el-col>
        <el-col :span="24">
          <el-table :data="pdfList">
            <el-table-column
              align="center"
              label="合同文件名称"
              prop="contentPdfName"
            />
            <el-table-column
              align="center"
              label="上传人"
              prop="realname"
              width="70"
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="160"
            >
              <template #default="{ row, $index }">
                <el-button
                  type="text"
                  @click="handleDownFilePDf(row)"
                  size="mini"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  @click="handlePreviewFilePDf(row)"
                  size="mini"
                >
                  预览
                </el-button>
                <el-button
                  v-if="allDisable"
                  type="text"
                  :key="'del' + $index"
                  @click="handleDeleteFilePDF(row, $index)"
                  size="mini"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <!--
        <el-col :span="24">
          <el-divider>审核合同文本</el-divider>
        </el-col> -->
        <!-- <el-col :span="24">
          <el-upload
            class="upload-demo"
            :show-file-list="false"
            :action="baseApi + api2 + '?contractId=' + contractid"
            :headers="headers"
            :on-preview="handlePreview"
            :on-success="handleSuccess2"
            :file-list="pdfList2"
            :before-upload="handleBeforeUpload"
            style="text-align: right"
          >
            <el-button type="primary" :disabled="!contractid">上传</el-button>
          </el-upload>
        </el-col> -->
        <!-- <el-col :span="24">
          <el-table :data="pdfList2">
            <el-table-column
              align="center"
              label="序号"
              type="index"
              width="50"
            ></el-table-column>
            <el-table-column
              align="center"
              label="合同文件名称"
              prop="fileName"
            />
            <el-table-column
              align="center"
              label="上传人"
              prop="uploaderName"
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleDownFilePDf2(row)"
                  size="mini"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  @click="handlePreviewFilePDf2(row)"
                  size="mini"
                >
                  预览
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>

        <el-col :span="24">
          <el-divider>附件</el-divider>
          <AttachList
            :readonly="readonly"
            :att-list="formData.attList"
            :local-list="localList"
            @delete-att="handleDeleteAtt"
            @upload-success="handleUploadSuccess"
            :att-type="2"
          />
        </el-col> -->
      </el-form>
    </el-row>
    <template #footer v-if="allDisable">
      <el-button @click="closed">取 消</el-button>
      <el-button type="primary" @click="save" :loading="buttonLoading">
        确 定
      </el-button>
      <el-button
        v-if="
          (formData.contractstatus == 2 || formData.contractstatus == 3) &&
          jurisdictionCode == 1
        "
        @click="ymsubmit"
        type="primary"
      >
        提交
      </el-button>
    </template>
    <xdf-options ref="xdf" @selected="handleXdfSelected" />
    <bank-options ref="bank" @selected="handleBankSelected" />
    <setup-options ref="setup" @selected="handleSetupSelected" />
    <unit-options ref="unit" @selected="handleUnitSelected" />
    <executor-options ref="executor" @selected="handleExecutorSelected" />
    <!-- <department-options ref="department" @selected="handleDepartmentSelected" /> -->
    <!-- <SealDepartment ref="department" @selected="handleDepartmentSelected" /> -->
    <TextEditor ref="webOffice" />
    <xdfInfo ref="xdfINfo" @selected="handleXdfInfoSelected" />
    <oaList ref="oaList" @selected="handleOA" />
    <TemplateTable ref="templateTable" @selected="handleTemplateSelected" />

    <!-- 提交 -->
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />

    <CreateDetail ref="common" />
  </el-dialog>
</template>

<script>
  import {
    checkStatus,
    deleteAttach,
    getContractItem,
    getContractTemplates,
    getContractTypes,
    saveContract,
    generateNo,
    deleteContractPdf,
    getCyhwUnitOAList,
    remoceOaDocument,
    contractPdfList,
    getPrivewAttInfo,
    downContractPdf,
    getOaurl,
    contractExamList,
    deleteContractExam,
    downContractExam,
    recallContractModify,
  } from '@/api/contract/manage'
  import CreateDetail from './CreateDetail.vue'
  import UEditor from '@/components/UEditor'
  import BankOptions from '../options/bank.vue'
  // import ExecutorOptions from '../options/executor.vue'
  import ExecutorOptions from '@/components/CompanySelectUserByTree'
  import SetupOptions from '../options/setupInfo.vue'
  import UnitOptions from '../options/unit.vue'
  import XdfOptions from '../options/xdf.vue'
  import oaList from '../options/oaList.vue'
  // import DepartmentOptions from '../options/department.vue'
  import { number2text } from '@/utils'
  import { contractStatusOptions } from '@/views/contract/consts'
  import AttachList from '../AttachList.vue'
  import JieduanList from '../block/JieduanList.vue'
  import SubList from '../block/SubList.vue'
  // import SealDepartment from '../options/sealDepartment.vue'
  import { comboFields } from './methods'
  import TextEditor from './TextEditor.vue'
  import xdfInfo from '../options/xdf.vue'
  import TemplateTable from './TemplateTable.vue'

  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import { getFaqiInfo } from '@/api/setting/msg'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  const { baseURL } = require('@/config')
  import store from '@/store'

  export default {
    name: 'ContractCommon',
    components: {
      CreateDetail,
      UEditor,
      XdfOptions,
      BankOptions,
      SetupOptions,
      UnitOptions,
      ExecutorOptions,
      // SealDepartment,
      SubList,
      JieduanList,
      AttachList,
      TextEditor,
      xdfInfo,
      oaList,
      CandidateUserSelect,
      Resubmit,
      TemplateTable,
    },
    provide() {
      return {
        fatherFetchItem: this.fetchItem,
      }
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/contract/importContractPdf',
        headers: {
          token: store.getters['user/token'],
        },
        executorType: '',
        showLoading: false,
        showEditor: false,
        beenClose: false,
        templates: [],
        currentEdit: 'moren',
        showSubFields: false,
        buttonLoading: false,
        subTitle: '',
        subFields: [
          { value: 'infoname', label: '货物名称' },
          { value: 'infodesc', label: '运输说明' },
          { value: 'infoxh', label: '规格型号' },
          { value: 'infoprice', label: '货物价值' },
        ],
        formFields: {
          changetype: undefined,
        },
        Jie: true,

        localList: [], // 本地缓存新增的附件列表
        uploadUrl: '',
        formData: {
          flowId: 622325,
          flowid: 622325,
          flowname: 'HTGL002',
          recordtype: 'HTGL002',
          contractid: undefined,
          contractno: '',
          contractname: undefined,
          contractitem: undefined,
          contracttype: null,
          startdate: undefined,
          enddate: undefined,
          counterparttype: undefined,
          recordparent: undefined,
          contractdatetype: undefined,
          contractxz: undefined,
          contractbd: undefined,
          contractzd: undefined,
          zxunit: undefined, // 执行单位key
          orgname: undefined, // 执行单位value
          contractdept: undefined, // 执行部门key
          orgmeno: undefined, // 执行部门value
          contractstaff: undefined, // 执行人key
          realname: undefined, // 执行人value
          contractxdfxinfo: undefined, //相对方主键key
          budgetname: undefined, // 相对方value
          topicname: undefined, // 立项信息key（是name）
          topicid: undefined, // 立项信息value
          counterpartbank: undefined, // 银行key
          bankaccount: undefined, // 银行value
          bankkhyh: undefined, // 开户银行
          momoconcat: undefined,
          dctype: undefined,
          contractmoney: undefined,
          moneytype: '人民币',
          hzsumowing: undefined,
          describe: undefined,
          nodeList: [],
          informationList: [],
          isbigmatter: '是',
          matterorg: undefined,
          contractstatus: 0,
        },
        readonly: false,
        rules: {
          changetype: [
            {
              required: true,
              message: '请选择合同变更类型',
              trigger: 'blur',
            },
          ],
          changedate: [
            {
              required: true,
              message: '请选择合同变更时间',
              trigger: 'blur',
            },
          ],
          changedesc: [
            {
              required: true,
              message: '请输入合同变更内容',
              trigger: 'blur',
            },
          ],
          contractname: [
            {
              required: true,
              message: '请输入合同名称',
              trigger: 'blur',
            },
          ],
          contractitem: [
            {
              required: false,
              message: '请输入项目编号',
              trigger: 'blur',
            },
          ],
          contracttype: [
            {
              required: true,
              message: '请输入合同类型',
              trigger: 'blur',
            },
          ],
          matterorg: [
            {
              required: true,
              message: '请选择',
              trigger: 'blur',
            },
          ],
          isbigmatter: [
            {
              required: true,
              message: '请选择',
              trigger: 'blur',
            },
          ],
          realname: [
            {
              required: true,
              message: '请选择承办人',
              trigger: 'change',
            },
          ],
          dctype: [
            {
              required: true,
              message: '请选择收付款方向',
              trigger: 'blur',
            },
          ],
          contractmoney: [
            {
              required: true,
              message: '请输入合同金额(元)',
              trigger: 'blur',
            },
          ],
          // topicid: [
          //   {
          //     required: true,
          //     message: '请选择项目名称',
          //     trigger: 'blur',
          //   },
          // ],
        },
        title: '',
        dialogFormVisible: false,
        options: [],
        pdfList: [],
        pdfList2: [],
        typeOptions: [],
        xdfOptions: [],
        changeOptions: [
          {
            label: '合同名称变更',
            value: '合同名称变更',
          },
          {
            label: '合同项目变更',
            value: '合同项目变更',
          },
          {
            label: '合同期限变更',
            value: '合同期限变更',
          },
          {
            label: '相对方变更',
            value: '相对方变更',
          },
          {
            label: '执行信息变更',
            value: '执行信息变更',
          },
          {
            label: '合同金额变更',
            value: '合同金额变更',
          },
          {
            label: '合同内容变更',
            value: '合同内容变更',
          },
          {
            label: '全部信息变更',
            value: '全部信息变更',
          },
        ],
        contractLevelOptions: [
          { label: '一般', value: '1' },
          { label: '重要', value: '2' },
          { label: '重大', value: '3' },
        ],
        goalStatus: 0,
        disabled: false,
        contractid: '',
        fromMine: false,
        tableDataProject: [],
        tableDataOA: [],
        SSoptions: [
          {
            value: '甲',
            label: '甲',
          },
          {
            value: '乙',
            label: '乙',
          },
          {
            value: '丙',
            label: '丙',
          },
          {
            value: '丁',
            label: '丁',
          },
          {
            value: '戊',
            label: '戊',
          },
          {
            value: '己',
            label: '己',
          },
          {
            value: '庚',
            label: '庚',
          },

          {
            value: '辛',
            label: '辛',
          },
          {
            value: '壬',
            label: '壬',
          },
          {
            value: '癸',
            label: '癸',
          },
        ],
        JDDetailXDFInfo: {},
        oaurl: '',
        ticket: '',
        //提交
        jurisdictionCode: 0,
        fromId: 0,
        fromIdcopy: 0,
        ymFromId: 0,
        flowtaskinfoflowid: '',
        status: '',
        preContractId: '',

        allDisable: true,
      }
    },
    computed: {
      showPlanList() {
        if (this.currentEdit !== 'moren') {
          if (this.currentEdit == 'zhanlue') {
            return false
          }
          return true
        } else {
          if (this.formData.contractplan) {
            return true
          }
          return false
        }
      },
    },
    watch: {
      'formData.nodeList'(val, val2) {
        if (val != val2) {
          // 移除组件
          this.Jie = false
          // 在组件移除后，重新渲染组件
          // this.$nextTick可实现在DOM 状态更新后，执行传入的方法。
          this.$nextTick(() => {
            this.Jie = true
            this.$refs['jieduanListRef'].fetchData()
          })
        }
      },
      'formData.matterorg': {
        handler(val) {
          if (val && val == '董事会') this.formData.isbigmatter = '是'
        },
      },
      'formData.changetype'(val) {
        if (!this.allDisable) {
          Object.keys(this.formFields).forEach((key) => {
            this.formFields[key].disabled = true
          })
        } else {
          if (val) {
            // this.pdfList = []
            Object.keys(this.formFields).forEach((key) => {
              this.formFields[key].disabled = true
            })
            if (val == '合同名称变更') {
              this.formFields['contractname'].disabled = false
              // this.readonly = true
              return
            }
            if (val == '合同项目变更') {
              this.formFields['topicid'].disabled = false
              // this.readonly = true
              return
            }
            if (val == '合同期限变更') {
              this.formFields['startdate'].disabled = false
              this.formFields['enddate'].disabled = false
              // this.readonly = true
              return
            }
            if (val == '相对方变更') {
              this.formFields['budgetname'].disabled = false
              // this.readonly = true
              return
            }
            if (val == '签约主体变更') {
              this.formFields['budgetname'].disabled = false
              // this.readonly = true
              return
            }
            if (val == '执行信息变更') {
              this.formFields['orgname'].disabled = false
              this.formFields['realname'].disabled = false
              this.formFields['orgmeno'].disabled = false
              // this.readonly = true
              return
            }
            if (val == '合同金额变更') {
              this.formFields['contractmoney'].disabled = false
              // this.readonly = true
              return
            }
            if (val == '合同内容变更') {
              this.formFields['momoconcat'].disabled = false
              // this.readonly = true\
            }
            if (val == '全部信息变更') {
              // this.readonly = false
              Object.keys(this.formFields).forEach((key) => {
                this.formFields[key].disabled = false
              })
            }
          }
        }
      },
    },
    created() {
      // this.fetchTypes()
    },
    methods: {
      openDetailRecord(id) {
        this.$refs['common'].showDetail({ contractid: id })
      },
      //过滤
      dctypeChange(val) {
        if (val == '无') {
          this.formData.contractmoney = 0
          this.formData.hzsumowing = '零元整'
        }
      },
      //过滤
      jijiatypeChange(val) {
        if (val == '无金额') {
          this.formData.contractmoney = 0
        }
        if (val == '非固定总价') {
          this.rules.contractmoney = [
            {
              required: false,
              message: '请输入合同金额(元)',
              trigger: 'blur',
            },
          ]
        } else {
          this.rules.contractmoney = [
            {
              required: true,
              message: '请输入合同金额(元)',
              trigger: 'blur',
            },
          ]
        }
      },
      //office
      openWebOffice() {
        //
        this.$store.commit('acl/contractidd', this.contractid)
        this.$refs['webOffice'].show()
      },
      executorShow(e) {
        if (e) {
          this.executorType = e
        } else {
          this.executorType = ''
        }
        this.$refs['executor'].show()
      },
      async fetchTypes() {
        const res = await getContractTypes()
        this.typeOptions = res.typeofList.map((item) => {
          return {
            label: item.typename,
            value: item.typeid,
          }
        })
      },
      async handleOADelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await remoceOaDocument({
            documentId: row.documentId,
          })
          if (res.code == 1) {
            this.$baseMessage('删除成功', 'success', 'vab-hey-message-success')
            await this.getOAList(this.contractid)
          }
        })
      },
      openOA() {
        if (!this.contractid) {
          this.$baseMessage('请先保存合同', 'error', 'vab-hey-message-success')
          return
        }
        this.$refs.oaList.show(this.contractid)
      },
      handleOA() {
        this.getOAList(this.contractid)
      },
      async getOAList(contractid) {
        const oaList = await getCyhwUnitOAList({
          contractId: contractid,
        })
        this.tableDataOA = oaList.data
      },
      async getOATicket() {
        const res = await getOaurl()
        this.ticket = res.data.ticket
        this.oaurl = res.data.oaurl.substring(0, res.data.oaurl.length - 1)
        //
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      handleDetail(row) {
        if (this.$store.state.work.processMobile) {
          window.open(`${this.oaurl}${row.h5url}&ticket=${this.ticket}`)
        } else {
          window.open(`${this.oaurl}${row.url}&ticket=${this.ticket}`)
        }
      },
      async fetchItem(row) {
        console.log('fetchItem row', row)
        const fileres = await contractPdfList({
          contractId: row.contractid,
          pageNumber: 1,
          pageSize: 20,
        })
        this.pdfList = fileres.data.tlist || []

        const fileres2 = await contractExamList({
          id: row.contractid,
          pageNumber: 1,
          pageSize: 20,
        })
        this.pdfList2 = fileres2.data.tlist || []

        this.contractid = row.contractid

        const res = await getContractItem({
          contractId: row.contractid,
          flowId: this.formData.flowId,
          flowname: row.recordtype,
        })

        this.preContractId = res.data.tcu.preContractId
        // this.getOAList(row.contractid)
        // this.getOATicket()
        Object.keys(this.formData).forEach((key) => {
          this.formData[key] = res.data.tcu[key]
        })
        this.buttonLoading = false
        if (this.formData.jijiatype == '非固定总价') {
          this.rules.contractmoney = [
            {
              required: false,
              message: '请输入合同金额(元)',
              trigger: 'blur',
            },
          ]
        } else {
          this.rules.contractmoney = [
            {
              required: true,
              message: '请输入合同金额(元)',
              trigger: 'blur',
            },
          ]
        }
        this.formData.isbigmatter = res.data.tcu.isbigmatter || '是'
        this.JDDetailXDFInfo = res //保存合同阶段相对方列表需要的信息
        this.formData.moneytype = res.data.tcu.moneytype
          ? res.data.tcu.moneytype
          : '人民币'
        this.formData.entrustStaffName = res.data.tcu.entrustStaffName
        this.formData.entrustStaffId = res.data.tcu.entrustStaffId
        this.formData.flowId = res.data.flowid
        this.formData.nodeList = res.data.nodeList
        this.formData.informationList = res.data.informationList
        this.formData.attList = res.data.attList
        this.formData.preContractId = res.data.tcu.preContractId || ''
        this.formData.contractid = res.data.tcu.contractid || ''
        this.formData.contractstatus = res.data.tcu.contractstatus
        const { bankinfo } = res.data.tcu
        if (bankinfo) {
          this.formData.counterpartbank = bankinfo.bankid
          this.formData.bankaccount = bankinfo.bankaccount
          this.formData.bankkhyh = bankinfo.bankkhyh
        }
        const info =
          res.data.tcu.budgetList &&
          res.data.tcu.budgetList.map((res) => {
            return {
              budgetname: res.budgetname,
              bugetId: res.budgetid,
              bugetType: res.budgettype,
            }
          })
        this.tableDataProject = info || []
        // 重置localList
        this.localList = []
        // if (this.fromMine) {
        //   const zzz = await generateNo({
        //     flowId: this.formData.flowId,
        //     contractNo: res.data.tcu.contractno,
        //   })
        //   this.formData.contractno = zzz.data.contractno
        //   this.$forceUpdate()
        // }
        this.showLoading = false
      },
      //过滤
      mapContractStatus(row) {
        const res = contractStatusOptions.filter((item) => {
          return item.value === row.contractstatus
        })
        return res[0].label
      },
      async getNo() {
        const res = await generateNo({
          flowId: this.formData.flowId,
        })
        this.formData.contractno = res.data.contractno
      },
      showDetail(row, type) {
        this.title = '详情'
        this.allDisable = false
        const {
          formData,
          formFields,
          subFields,
          showSubFields,
          subTitle,
          currentEdit,
        } = comboFields(type, true)
        this.formData = formData
        this.formFields = { ...formFields }
        Object.keys(this.formFields).forEach((key) => {
          if (key.indexOf('change') < 0) {
            this.formFields[key].disabled = true
          }
        })

        this.currentEdit = currentEdit
        this.subFields = subFields
        this.showSubFields = showSubFields
        this.subTitle = subTitle
        this.fetchItem(row)
        this.dialogFormVisible = true
        this.beenClose = false
        this.showEditor = false
        this.showLoading = false
      },
      async showEdit(row, type, fromMine, goalStatus = 0) {
        // this.getNo()
        console.log('showEdit', row)
        this.goalStatus = goalStatus
        if (goalStatus == 10) {
          this.setOffice(row)
        }
        this.showLoading = true
        this.allDisable = true
        this.title = '编辑'
        this.fromMine = fromMine
        // fromMine: 我的合同-合同变更过来的，不需要检测合同状态是否允许修改
        if (this.mapContractStatus(row) != '需调整' && !fromMine) {
          if (!row.contractid) return
          const { code } = await checkStatus({ contractId: row.contractid })
          if (code != 1) {
            return
          }
        }

        const {
          formData,
          formFields,
          subFields,
          showSubFields,
          subTitle,
          currentEdit,
        } = comboFields(type, true)
        this.formData = formData
        this.formFields = { ...formFields }
        Object.keys(this.formFields).forEach((key) => {
          if (key.indexOf('change') < 0) {
            this.formFields[key].disabled = true
          }
        })

        if (row.contractstatus == 2 || row.contractstatus == 3) {
          const res2 = await getFlowTaskInfo({
            tableId: 7,
            formId: row.contractid,
          })
          this.jurisdictionCode = res2.data.isFlowInfo
          if (res2.data.isFlowInfo) {
            this.flowtaskinfoflowid = res2.data.flowId + ''
            this.fromId = row.contractid + ''
            this.fromIdcopy = row.contractid + ''
            this.ymFromId = res2.data.id + ''

            const res3 = await getFaqiInfo({
              id: res2.data.id,
              flowId: res2.data.flowId,
            })
            if (res3.code == 1) {
              this.status = res3.data.dataJson.flowTaskInfo.status + ''
            }
          }
        }

        this.currentEdit = currentEdit
        this.subFields = subFields
        this.showSubFields = showSubFields
        this.subTitle = subTitle
        this.fetchItem(row)
        this.dialogFormVisible = true
        this.beenClose = false
        this.showEditor = false
        this.renderTemplates(type)
        this.showLoading = false
      },
      setOffice(row) {
        // console.log('zzz', row)
        const info = JSON.parse(localStorage.getItem('userInfo'))
        // let temp = document.createElement('form')
        // temp.action = `http://office.wenxin.example.com/api/office/updateContractCopy?fileType=word&parentId=${row.preContractId}&contractId=${row.contractid}&uid=${info.staffid}&name=${info.username}`
        // temp.method = 'post'
        // temp.target = '_blank'
        // document.body.appendChild(temp)
        // temp.submit()

        var url = `https://office.wenxin.example.com/api/office/updateContractCopy?fileType=word&parentId=${row.preContractId}&contractId=${row.contractid}&uid=${info.staffid}&name=${info.username}`
        var httpRequest = new XMLHttpRequest()
        httpRequest.open('POST', url, true)
        // httpRequest.setRequestHeader("Content-type", "application/json");
        var obj = {
          // "username": "mkii",
          // "password": "1234"
        }

        httpRequest.send(JSON.stringify(obj))

        // var url = `http://office.wenxin.example.com/api/office/updateContractCopy?fileType=word&parentId=${row.preContractId}&contractId=${row.contractid}&uid=${info.staffid}&name=${info.username}`
        // var httpRequest = new XMLHttpRequest()
        // httpRequest.open('POST', url, true)
        // httpRequest.setRequestHeader('Content-type', 'application/json')
        // var obj = {}

        // httpRequest.send(JSON.stringify(obj))
      },
      async renderTemplates(type) {
        // 获取范本模板列表
        const data = await getContractTemplates({
          contractType: type,
        })
        if (data && data.length) {
          this.templates = data.map((item) => {
            return {
              pre: 'pre1.png',
              title: '',
              preHtml: item.contractname,
              html: item.describe,
            }
          })
        }
        this.showEditor = true
      },
      handleXdfSelected(val, field) {
        if (field == 'budgetname') {
          this.formData.contractxdfxinfo = val.budgetid
          this.formData.budgetname = val.budgetname
          this.formData.counterparttype = val.counterparttype
          this.formData.director = val.director
          this.formData.counterpartaddress = val.counterpartaddress
          this.formData.contacts = val.contacts
          this.formData.contactsphone = val.contactsphone
        } else {
          this.formData.recordparent = val.contractid
          this.formData.parentname = val.contractname
        }
      },
      //回调
      handleChoseBankClick() {
        if (!this.formData.contractxdfxinfo) {
          const label = this.formFields['budgetname'].label
          this.$baseMessage(
            `请先选择${label}！`,
            'error',
            'vab-hey-message-error'
          )
          return
        }
        this.$refs.bank.show({
          budgetId: this.formData.contractxdfxinfo,
        })
      },
      //回调
      handleBankSelected(node) {
        this.formData.counterpartbank = node.bankid
        this.formData.bankaccount = node.bankaccount
        this.formData.bankkhyh = node.bankkhyh
      },
      //回调
      handleSetupSelected(node) {
        this.formData.topicname = node.projectname
        this.formData.topicid = node.projectid
      },
      //回调
      handleUnitSelected(node) {
        this.formData.zxunit = node.id
        this.formData.orgname = node.label
        this.$forceUpdate()
      },
      //回调
      handleExecutorSelected(node) {
        if (this.executorType !== '') {
          this.$set(this.formData, this.executorType + 'Id', node.staffid)
          this.$set(this.formData, this.executorType + 'Name', node.realname)
        } else {
          this.formData.realname = node.realname
          this.formData.contractstaff = node.staffid
        }
        this.$forceUpdate()
      },
      //回调
      handleDepartmentSelected(node) {
        this.formData.contractdept = node.id
        this.formData.orgmeno = node.name
      },
      handleBlur() {
        this.formData.hzsumowing = number2text(this.formData.contractmoney)
      },
      handleUploadSuccess(val) {
        this.localList.push(val.data)
      },
      async handleDeleteAtt(row, index) {
        const { code } = await deleteAttach({
          attid: row.attid,
        })
        if (code == 1) {
          if (row.type == 'local') {
            this.localList.splice(index - this.formData.attList.length, 1)
          } else {
            this.formData.attList.splice(index, 1)
          }
        }
      },
      handleDeleteSub(index) {
        this.formData.informationList.splice(index, 1)
      },
      closed() {
        this.dialogFormVisible = false
      },
      async close() {
        console.log('close')
        if (this.goalStatus == 10) {
          const { code } = await recallContractModify({
            contractId: this.formData.contractid,
            flowname: 'HTGL005',
          })
        }
        this.$refs['form'].resetFields()
        this.clearType = true
        this.$emit('fetch-data')
        this.formData = this.$options.data().formData
        this.dialogFormVisible = false
        this.beenClose = true
        this.tableDataProject = []
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.buttonLoading = true
            const arrAttid = this.localList.map((item) => item.attid)
            if (arrAttid && arrAttid.length) {
              this.formData.attids = arrAttid.join(',')
            }
            const { describe, budgetList, ...other } = this.formData

            this.$delete(this.formData, 'flowid')

            // TODO 如果我的合同-合同变更过来需要用另外的api  fromMine
            const api = this.fromMine ? saveContract : saveContract
            // const api = this.fromMine ? 'XXX' : saveContract
            const { msg, data, code } = await api({
              ...other,
              contractxdf: JSON.stringify(this.tableDataProject),
            })

            if (code == 1) {
              // this.$store.commit('acl/contractidd', data.contractid)
              this.preContractId = data.preContractId
              this.$baseMessage('保存成功', 'success')
              this.goalStatus = 0
              this.$emit('fetch-data')
              this.fetchItem(data, '')
            } else {
              this.$baseMessage(msg, 'error')
              this.buttonLoading = false
            }
            // this.formData = data
          }
        })
      },
      handleDelete(index, row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(async () => {
            this.tableDataProject.splice(index, 1)
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
      // 添加点击按钮
      handleAdds() {
        this.tableDataProject.push({
          budgetname: '',
          bugetType: '',
        })
      },
      //回调
      handleXdfInfoSelected(info, b, c) {
        this.tableDataProject[c].budgetname = info.budgetname
        this.tableDataProject[c].bugetId = info.budgetid
      },
      async handleDownFilePDf(list) {
        const data = await downContractPdf({
          contentPdfId: list.contentPdfId,
        })
        let filename = list.contentPdfName
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          // type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      //回调
      async handleDownFilePDf2(list) {
        const data = await downContractExam({
          id: list.id,
        })
        let filename = list.fileName
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          // type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      handleBeforeUpload(file) {
        // if (this.pdfList.length > 0) {
        //   this.$baseMessage('只能上传一个文件,请先删除！', 'error')
        //   return false
        // }
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      handlePreview(file) {},
      //上传
      handleSuccess(file) {
        if (file.code == '1') {
          this.pdfList.push(file.data)
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      async handlePreviewFilePDf(list) {
        const { data } = await getPrivewAttInfo({
          attId: list.contentPdfId,
          attType: 4,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },
      async handlePreviewFilePDf2(list) {
        const { data } = await getPrivewAttInfo({
          attId: list.id,
          attType: 5,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },
      async handleDeleteFilePDF(list, index) {
        await deleteContractPdf({ contentPdfId: list.contentPdfId })
        this.pdfList.splice(index, 1)
        this.$forceUpdate()
        this.$baseMessage('删除成功', 'success')
      },
      async handleDeleteFilePDF2(list, index) {
        await deleteContractExam({ id: list.id })
        this.pdfList2.splice(index, 1)
        this.$forceUpdate()
        this.$baseMessage('删除成功', 'success')
      },
      //提交
      async ymsubmit() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
      //office
      openOffice() {
        this.$store.commit('acl/contractidd', this.contractid)
        const info = JSON.parse(localStorage.getItem('userInfo'))
        let temp = document.createElement('form')
        temp.action = `https://office.wenxin.example.com/api/office/editRealContract?fileType=word&contractId=${this.contractid}&uid=${info.staffid}&name=${info.username}`
        temp.method = 'post'
        temp.target = '_blank'
        document.body.appendChild(temp)
        temp.submit()
      },
      handleoffice() {
        const info = JSON.parse(localStorage.getItem('userInfo'))
        window.open(
          `https://office.wenxin.example.com/api/office/getContractReview?fileType=word&contractId=${this.contractid}&uid=${info.staffid}&name=${info.username}`
        )
      },
      //打开模板
      openTemplate() {
        this.$refs['templateTable'].show(this.formData.contracttype)
      },
      //回调
      handleTemplateSelected(val) {
        this.templateId = val.contractid
        this.$store.commit('acl/contractidd', val.contractid)
        this.$store.commit('acl/contractidd2', this.contractid)
        const info = JSON.parse(localStorage.getItem('userInfo'))
        let temp = document.createElement('form')
        temp.action = `https://office.wenxin.example.com/api/office/editRealContract?fileType=word&templateId=${val.contractid}&contractId=${this.contractid}&uid=${info.staffid}&name=${info.username}`
        temp.method = 'post'
        temp.target = '_blank'
        document.body.appendChild(temp)
        temp.submit()
      },
      handleSuccess1() {
        this.openOffice()
      },
      handleFileBefore(file) {
        return new Promise((resolve, reject) => {
          const info = JSON.parse(localStorage.getItem('userInfo'))
          // 此处动态配置action URL
          this.uploadUrl = `https://office.wenxin.example.com/api/office/editUpdateContract?fileType=word&contractId=${this.contractid}&uid=${info.staffid}&name=${info.username}&upload=true`
          // dom上传地址更新完成后，触发上传
          this.$nextTick(() => resolve())
        })
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
  .el-table {
    margin-top: 10px;
    margin-bottom: 18px;
  }
  .el-form .el-form-item .el-button {
    position: absolute;
  }
</style>
