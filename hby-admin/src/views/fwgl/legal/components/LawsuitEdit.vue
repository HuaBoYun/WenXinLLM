<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    append-to-body
    @close="close"
  >
    <el-row :gutter="15">
      <el-form ref="form" label-width="140px" :model="form" :rules="rules">
        <el-row>
          <el-col :span="24">
            <el-col :span="12">
              <el-form-item label="关联纠纷登记">
                <span v-if="show">{{ form.disputeItem }}</span>
                <span v-else>
                  <el-input
                    v-model="form.disputeItem"
                    clearable
                    placeholder="请选择纠纷登记"
                    readonly
                    :style="{ width: '256px' }"
                  />
                  <el-button
                    :style="{ marginLeft: '10px' }"
                    type="primary"
                    @click="$refs.xzjf.show(2)"
                  >
                    选择
                  </el-button>
                </span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="关联仲裁">
                <span v-if="show">{{ form.courtfirst }}</span>
                <span v-else>
                  <el-input
                    v-model="form.courtfirst"
                    clearable
                    placeholder="请选择关联仲裁"
                    readonly
                    :style="{ width: '256px' }"
                  />
                  <el-button
                    :style="{ marginLeft: '10px' }"
                    type="primary"
                    @click="$refs.glzc.show()"
                  >
                    选择
                  </el-button>
                </span>
              </el-form-item>
            </el-col>
          </el-col>

          <el-col :span="12">
            <el-form-item label="填报单位" prop="fillunit">
              <span v-if="show">{{ form.fillunit }}</span>
              <span v-else>
                <el-input
                  v-model.trim="form.fillunit"
                  clearable
                  disabled
                  placeholder="请输入填报单位"
                  :style="{ width: '100%' }"
                />
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="案由" prop="causecase">
              <span v-if="show">{{ form.causecase }}</span>
              <span v-else>
                <el-input
                  v-model="form.causecase"
                  type="textarea"
                  rows="2"
                ></el-input>
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纠纷类型" prop="disputetype">
              <span v-if="show">{{ form.disputetype }}</span>
              <span v-else>
                <el-select
                  v-model="form.disputetype"
                  :style="{ width: '100%' }"
                >
                  <el-option value="一般纠纷"></el-option>
                  <el-option value="重大纠纷"></el-option>
                </el-select>
              </span>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="律师事务所" prop="lawfirm">
              <span v-if="show">{{ form.lawfirm }}</span>
              <span v-else>
                <el-input v-model="form.lawfirm"></el-input>
              </span>
            </el-form-item>
          </el-col> -->

          <el-col :span="12">
            <el-form-item label="关联合同" prop="islinkcontract">
              <span v-if="show">{{ form.islinkcontract ? '是' : '否' }}</span>
              <span v-else>
                <el-select
                  v-model="form.islinkcontract"
                  :style="{ width: '100%' }"
                >
                  <el-option :value="1" label="是"></el-option>
                  <el-option :value="0" label="否"></el-option>
                </el-select>
              </span>
            </el-form-item>
          </el-col>

          <el-col :span="12" v-if="form.islinkcontract === '1'">
            <el-form-item label="合同名称" prop="contractname">
              <span v-if="show">{{ form.contractname }}</span>
              <span v-else>
                <el-input
                  v-model.trim="form.contractname"
                  clearable
                  placeholder="请选择合同名称"
                  readonly
                  :style="{ width: '256px' }"
                />
                <el-button
                  :style="{ marginLeft: '10px', position: 'absolute' }"
                  type="primary"
                  @click="$refs.xzht.show()"
                >
                  选择
                </el-button>
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.islinkcontract === '1'">
            <el-form-item label="合同编号" prop="contractno">
              <span v-if="show">{{ form.contractno }}</span>
              <span v-else>
                <el-input
                  v-model.trim="form.contractno"
                  clearable
                  placeholder="请输入合同编号"
                  readonly
                  :style="{ width: '100%' }"
                />
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.islinkcontract === '1'">
            <el-form-item label="合同执行人">
              <span v-if="show">{{ form.realname }}</span>
              <span v-else>
                <el-input
                  v-model.trim="form.realname"
                  clearable
                  placeholder="请输入合同执行人"
                  readonly
                  :style="{ width: '100%' }"
                />
              </span>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="公司经办人" prop="disputeunderName">
              <span v-if="show">{{ form.disputeunderName }}</span>
              <span v-else>
                <el-input
                  v-model.trim="form.disputeunderName"
                  clearable
                  placeholder="请选择公司经办人"
                />
                <!-- <el-button
                  :style="{ marginLeft: '10px', position: 'absolute' }"
                  type="primary"
                  @click="$refs.executor.show()"
                >
                  选择
                </el-button> -->
              </span>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="诉讼地位" prop="litigiousstatus">
              <span v-if="show">{{ form.litigiousstatus }}</span>
              <span v-else>
                <el-select
                  v-model="form.litigiousstatus"
                  :style="{ width: '100%' }"
                >
                  <el-option label="原告" value="原告">原告</el-option>
                  <el-option label="被告" value="被告">被告</el-option>
                  <el-option label="上诉人" value="上诉人">上诉人</el-option>
                  <el-option label="被上诉人" value="被上诉人">
                    被上诉人
                  </el-option>
                  <el-option label="第三人" value="第三人">第三人</el-option>
                  <el-option label="再审申请认" value="再审申请人">
                    再审申请人
                  </el-option>
                  <el-option label="被申请人" value="被申请人">
                    被申请人
                  </el-option>
                </el-select>
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="我方涉诉企业" prop="ourdisputeorg">
              <span v-if="show">{{ form.ourdisputeorg }}</span>
              <span v-else>
                <el-input
                  v-model="form.ourdisputeorg"
                  placeholder="请输入我方涉诉企业"
                ></el-input>
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="对方当事人" prop="oppositeparty">
              <span v-if="show">{{ form.oppositeparty }}</span>
              <span v-else>
                <el-input
                  v-model="form.oppositeparty"
                  placeholder="请输入对方当事人"
                ></el-input>
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="涉诉金额（万元）" prop="subjectamount">
              <span v-if="show">{{ form.subjectamount }}</span>
              <span v-else>
                <el-input type="number" v-model="form.subjectamount"></el-input>
              </span>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="管辖机构" prop="jurisdiction">
              <span v-if="show">{{ form.jurisdiction }}</span>
              <span v-else>
                <el-input
                  v-model="form.jurisdiction"
                  placeholder="请输入管辖机构"
                ></el-input>
              </span>
            </el-form-item>
          </el-col> -->
          <!-- <el-col :span="12">
            <el-form-item label="纠纷状态" prop="disputestatus">
              <span v-if="show">
                {{
                  form.disputestatus == '1'
                    ? '一审审理中'
                    : form.disputestatus == '2'
                    ? '一审已判决'
                    : form.disputestatus == '3'
                    ? '二审审理中'
                    : form.disputestatus == '4'
                    ? '二审已判决'
                    : form.disputestatus == '5'
                    ? '再审审理中'
                    : form.disputestatus == '6'
                    ? '再审已判决'
                    : form.disputestatus == '7'
                    ? '强制执行'
                    : form.disputestatus == '8'
                    ? '破产重整'
                    : form.disputestatus == '9'
                    ? '仲裁审理中'
                    : form.disputestatus == '10'
                    ? '已裁决'
                    : form.disputestatus == '11'
                    ? '已结案'
                    : '诉前调解'
                }}
              </span>
              <span v-else>
                <el-select
                  v-model="form.disputestatus"
                  :style="{ width: '100%' }"
                >
                  <el-option :value="0" label="诉前调解"></el-option>
                  <el-option :value="1" label="一审审理中"></el-option>
                  <el-option :value="2" label="一审已判决"></el-option>
                  <el-option :value="3" label="二审审理中"></el-option>
                  <el-option :value="4" label="二审已判决"></el-option>
                  <el-option :value="5" label="再审审理中"></el-option>
                  <el-option :value="6" label="再审已判决"></el-option>
                  <el-option :value="7" label="强制执行"></el-option>
                  <el-option :value="8" label="破产重整"></el-option>
                  <el-option :value="9" label="仲裁审理中"></el-option>
                  <el-option :value="10" label="已裁决"></el-option>
                  <el-option :value="11" label="已结案"></el-option>
                </el-select>
              </span>
            </el-form-item>
          </el-col> -->
          <el-col :span="12">
            <el-form-item label="业务发生时间" prop="biztime">
              <span v-if="show">{{ form.biztime }}</span>
              <span v-else>
                <el-date-picker
                  v-model="form.biztime"
                  type="date"
                  :style="{ width: '100%' }"
                  placeholder="选择日期"
                  value-format="yyyy-MM-dd"
                ></el-date-picker>
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="立案时间" prop="casetime">
              <span v-if="show">{{ form.casetime }}</span>
              <span v-else>
                <el-date-picker
                  v-model="form.casetime"
                  type="date"
                  :style="{ width: '100%' }"
                  placeholder="选择日期"
                  value-format="yyyy-MM-dd"
                  align="bottom"
                  :picker-options="pickerOptions"
                ></el-date-picker>
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否涉及刑事案件" prop="iscriminalcases">
              <span v-if="show">
                {{ form.iscriminalcases == '1' ? '是' : '否' }}
              </span>
              <span v-else>
                <el-select
                  v-model="form.iscriminalcases"
                  :style="{ width: '100%' }"
                >
                  <el-option :value="1" label="是"></el-option>
                  <el-option :value="0" label="否"></el-option>
                </el-select>
              </span>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="对方当事人是否具备偿还能力" prop="isrepayment">
              <span v-if="show">
                {{
                  form.isrepayment == '1'
                    ? '有'
                    : form.isrepayment == '2'
                    ? '未知'
                    : '无'
                }}
              </span>
              <span v-else>
                <el-select
                  v-model="form.isrepayment"
                  :style="{ width: '100%' }"
                >
                  <el-option :value="2" label="未知"></el-option>
                  <el-option :value="1" label="有"></el-option>
                  <el-option :value="0" label="无"></el-option>
                </el-select>
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否有担保或保全措施" prop="ispreservation">
              <span v-if="show">
                {{ form.ispreservation == '1' ? '有' : '无' }}
              </span>
              <span v-else>
                <el-select
                  v-model="form.ispreservation"
                  :style="{ width: '100%' }"
                >
                  <el-option :value="1" label="有"></el-option>
                  <el-option :value="0" label="无"></el-option>
                </el-select>
              </span>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="执行状态" prop="execstatus">
              <span v-if="show">
                {{
                  form.execstatus == '1'
                    ? '账户冻结'
                    : form.execstatus == '2'
                    ? '查封'
                    : form.execstatus == '3'
                    ? '扣押'
                    : form.execstatus == '4'
                    ? '拍卖'
                    : form.execstatus == '5'
                    ? '变卖'
                    : form.execstatus == '6'
                    ? '协助执行'
                    : form.execstatus == '7'
                    ? '其他'
                    : ''
                }}
              </span>
              <span v-else>
                <el-select
                  v-model="form.execstatus"
                  :style="{ width: '100%' }"

                >
                  <el-option :value="1" label="账户冻结"></el-option>
                  <el-option :value="2" label="查封"></el-option>
                  <el-option :value="3" label="扣押"></el-option>
                  <el-option :value="4" label="拍卖"></el-option>
                  <el-option :value="5" label="变卖"></el-option>
                  <el-option :value="6" label="协助执行"></el-option>
                  <el-option :value="7" label="其他"></el-option>
                </el-select>
              </span>
            </el-form-item>
          </el-col> -->
          <el-col :span="12">
            <el-form-item label="是否为紧急事项" prop="isurgent">
              <span v-if="show">{{ form.isurgent == 1 ? '是' : '否' }}</span>
              <span v-else>
                <el-select v-model="form.isurgent" :style="{ width: '100%' }">
                  <el-option :value="1" label="是"></el-option>
                  <el-option :value="0" label="否"></el-option>
                </el-select>
              </span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="15">
          <el-col :span="24" v-if="form.disputestatus === '1'">
            <el-form-item label="紧急情况说明" prop="urgentmemo">
              <span v-if="show">{{ form.urgentmemo }}</span>
              <span v-else>
                <el-input
                  placeholder="请输入紧急情况说明"
                  :style="{ width: '100%' }"
                  type="textarea"
                  v-model="form.urgentmemo"
                  disabled
                ></el-input>
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="案件的基本情况" prop="casebasicinfo">
              <span v-if="show">{{ form.casebasicinfo }}</span>
              <span v-else>
                <el-input
                  v-model="form.casebasicinfo"
                  placeholder="请输入案件的基本情况"
                  :style="{ width: '100%' }"
                  type="textarea"
                />
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="企业采取的处理措施或诉讼思路"
              prop="takemeasures"
            >
              <span v-if="show">{{ form.takemeasures }}</span>
              <span v-else>
                <el-input
                  v-model="form.takemeasures"
                  placeholder="请输入企业采取的处理措施或诉讼思路"
                  :style="{ width: '100%' }"
                  type="textarea"
                />
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="判决或仲裁结果" prop="judgmentresult">
              <span v-if="show">{{ form.judgmentresult }}</span>
              <span v-else>
                <el-input
                  v-model="form.judgmentresult"
                  placeholder="请输入初步解决建议"
                  :style="{ width: '100%' }"
                  type="textarea"
                />
              </span>
            </el-form-item>
          </el-col>
        </el-row>
        <div style="text-align: right">
          <el-button v-if="show == 0" type="primary" @click="save">
            保 存
          </el-button>
        </div>
      </el-form>

      <!-- <el-col :span="24">
        <el-divider>我方代理人</el-divider>
      </el-col>
      <el-col :span="24">
        <div style="text-align: right; margin-bottom: 5px">
          <el-button @click="addLine" type="success">新增</el-button>
        </div>
        <el-table :data="dllsData" style="width: 100%; margin: 10px 0 50px 0%">
          <el-table-column align="center" prop="isattorney" label="是否外聘">
            <template #default="{ row }">
              {{ row.isattorney === 1 ? '是' : '否' }}
            </template>
          </el-table-column>
          <el-table-column align="center" prop="attorney" label="代理人" />
          <el-table-column
            align="center"
            prop="attorneyphont"
            label="联系方式"
          />
          <el-table-column align="center" prop="attorneyphont" label="操作">
            <template #default="{ row }">
              <el-button @click="deleteRow(row.id)" type="text" size="small">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col> -->

      <el-col :span="24">
        <el-divider>诉讼过程记录</el-divider>
      </el-col>
      <el-col :span="24">
        <div style="text-align: right; margin-bottom: 5px">
          <el-button type="success" @click="showEditAdd" v-if="!show">
            新增
          </el-button>
        </div>
        <el-table
          v-loading="listLoading"
          :data="list"
          style="margin-bottom: 20px"
        >
          <el-table-column align="center" label="案号" prop="proceedno">
            <template #default="{ row }">
              <el-button type="text" @click="handleDetailSsgc(row)">
                {{ row.proceedno }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="诉讼阶段"
            prop="porceedstage"
          />
          <el-table-column align="center" label="审理法院" prop="court" />
          <el-table-column align="center" label="法院联系人" prop="courtlink" />
          <el-table-column
            align="center"
            label="法院联系方式"
            prop="courtcontact"
          />
          <!-- <el-table-column align="center" label="立案时间" prop="filingtime" /> -->
          <el-table-column align="center" label="开庭时间" prop="openingtime" />
          <!-- <el-table-column
            align="center"
            label="收到判决时间"
            prop="judgetiem"
          /> -->
          <el-table-column align="center" label="状态" prop="state">
            <template #default="{ row }">
              {{
                row.state == 1
                  ? '审批中'
                  : row.state == 2
                  ? '已退回'
                  : row.state == 3
                  ? '已撤销'
                  : row.state == 4
                  ? '已终止'
                  : row.state == 5
                  ? '已跟踪'
                  : row.state == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" width="200">
            <template #default="{ row }">
              <el-button
                type="text"
                @click="handleApproval(row)"
                :disabled="row.state"
              >
                诉讼或再审
              </el-button>
              <el-button
                type="text"
                @click="handleEditSsgc(row)"
                :disabled="row.state != 0 && row.state != 2 && row.state != 3"
              >
                编辑
              </el-button>
              <el-button
                type="text"
                @click="handleDeleteSsgc(row)"
                :disabled="row.state"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>

      <el-col :span="24">
        <el-divider>附件</el-divider>
      </el-col>
      <el-col :span="24">
        <div style="text-align: right; margin-bottom: 5px">
          <el-button v-if="!form.litigationid" type="success" @click="hold()">
            上传
          </el-button>
          <el-upload
            v-else
            ref="upload"
            :accept="accept"
            :action="baseApi + api"
            :before-upload="handleBeforeUpload"
            :data="uploadData"
            :file-list="fileList"
            :headers="headers"
            :on-error="onError"
            :on-remove="handleRemove"
            :on-success="onSuccess"
          >
            <el-button type="success" v-if="!show">上传</el-button>
          </el-upload>
        </div>
        <el-table :data="uploadlist">
          <el-table-column align="center" label="附件名称" prop="attname" />
          <el-table-column align="center" label="文件大小(KB)" prop="attsize" />
          <el-table-column align="center" label="创建人" prop="uploader" />
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handlePreviewFile(row)">
                预览
              </el-button>
              <el-button type="text" @click="downloadData(row)">下载</el-button>
              <el-button type="text" @click="handleDelete1(row)" v-if="!show">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <template #footer>
      <div class="dialog_btn">
        <div>
          <el-button type="primary" @click="ccbqAdd">财产保全</el-button>
          <!-- <el-button type="primary" @click="zxglAdd">执行管理</el-button> -->
          <el-button type="primary" @click="jazjAdd">结案总结</el-button>
        </div>
        <div>
          <el-button @click="close">取 消</el-button>
          <el-button v-if="show == 0" type="primary" @click="save">
            确 定
          </el-button>
        </div>
      </div>
    </template>
    <!-- 纠纷登记 -->
    <xzjf-options ref="xzjf" @selected="handleSsjd" />
    <!-- 关联仲裁 -->
    <glzc-options ref="glzc" @selected="handleSelectGlzc" />
    <!-- 合同 -->
    <xzht-options ref="xzht" @selecteded="handleHt" />
    <!-- 财产保全 -->
    <zcbqModule ref="zcbqModuleref" :info="this.baseInfo" />
    <!-- 执行管理 -->
    <zxglModule ref="zxglModuleref" :info="this.baseInfo" />
    <!-- 结案 -->
    <jazjAdd ref="jazjAddref" />
    <lawsuitEdit ref="editadd" @editadd="handleeditadd" />
    <executor-options ref="executor" @selected="handleSelected" />
    <!-- 代理律师新增编辑 -->
    <lower-edit ref="lowerEdit" @fetch-lawer-list="fetchLawerList" />
    <ProcessList ref="process" @fetchData="fetchData" />
  </el-dialog>
</template>

<script>
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import {
    deleAttacheMent,
    findAttacheMent,
    legalAttorney,
    legalAttorneyDelete,
    litigationSettlementDetail,
    litigationSettlementModify,
    litigationSettlementSave,
    proceedingsRecord,
    removeLegalProceedingsRecord,
  } from '@/api/fwgl/legal'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { baseURL } from '@/config/net.config'
  import store from '@/store'
  import LowerEdit from '@/views/fwgl/legal/components/lowerEdit'
  import ExecutorOptions from '@/views/fwgl/legal/components/options/executor'
  import jazjAdd from './jazjAdd.vue'
  import glzcOptions from './options/glzc.vue'
  import xzhtOptions from './options/xzht.vue'
  import xzjfOptions from './options/xzjf.vue'
  import lawsuitEdit from './smEdit/lawsuitEdit2.vue'
  import zcbqModule from './zcbqModule.vue'
  import zxglModule from './zxglModule.vue'
  import { downloads } from '@/api/fwgl/zzxx'

  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  const orgname = (userInfo.currentOrg || {}).orgname || ''
  // this.form.fillunit = orgname

  export default {
    name: 'DraftEdit',
    components: {
      xzjfOptions,
      lawsuitEdit,
      xzhtOptions,
      ExecutorOptions,
      zcbqModule,
      jazjAdd,
      zxglModule,
      glzcOptions,
      LowerEdit,
      ProcessList,
    },
    data() {
      return {
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() > Date.now()
          },
          shortcuts: [
            {
              text: '今天',
              onClick(picker) {
                picker.$emit('pick', new Date())
              },
            },
            {
              text: '昨天',
              onClick(picker) {
                const date = new Date()
                date.setTime(date.getTime() - 3600 * 1000 * 24)
                picker.$emit('pick', date)
              },
            },
            {
              text: '一周前',
              onClick(picker) {
                const date = new Date()
                date.setTime(date.getTime() - 3600 * 1000 * 24 * 7)
                picker.$emit('pick', date)
              },
            },
          ],
        },
        form: {
          litigationid: undefined,
          firstcourt: undefined,
          presidingjudge: undefined,
          collegialpanel: undefined,
          shouliDate: undefined,
          actionobject: undefined,
          litigationamount: undefined,
          firsthearingdate: undefined,
          litigationenddate: undefined,
          iseffect: 1,
          judgemoney: undefined,
          disputeItem: undefined,
          courtfirst: undefined,
          litigationresult: undefined,
          disputeid: undefined,
          attids: undefined,
          dealdate: undefined,
          fillunit: orgname,
        },
        rules: {
          firstcourt: [
            {
              required: true,
              message: '请输入案件名称',
              trigger: 'blur',
            },
          ],
        },
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
          flowid: '698869',
        },
        title: '',
        dialogFormVisible: false,
        radio: '',
        litigationId: '',
        list: [],
        dllsData: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        show: 0,
        accept: '.pdf, .doc, .docx, .xls, .xlsx,.png,.jpg,.jpeg',
        api: '/contract/uploadFileAttInfo',
        // data: {
        //   attpath: '1649657363411.xlsx',
        //   attname: '1649657363411.xlsx',
        //   token: store.getters['user/token'],
        // },
        headers: {
          token: store.getters['user/token'],
          // 'Content-Type': 'application/x-www-form-urlencoded',
        },
        uploadlist: [],
        value: {},
        beforeUpload: null,
        baseApi: baseURL,
        fileList: [],
        baseInfo: {}, //传给孙子组件信息
      }
    },
    computed: {
      uploadData() {
        return {
          type: 3,
          bid: this.form.litigationid,
        }
      },
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.handleeditadd()
        }
      })
    },
    created() {},
    methods: {
      handleApproval(row) {
        //提交审批
        // this.$refs['process'].save(27, row.otherFileMessageId)
        const tableId = 30
        const fromId = row.proceedid
        this.$refs['process'].save(tableId, fromId)
      },
      /**
       * @description: 财产保全新增
       * @return {*}
       */
      ccbqAdd() {
        if (!this.form.litigationid) {
          this.$message({
            type: 'error',
            message: '请先保存表单',
          })
          return
        }
        this.$refs['zcbqModuleref'].show({
          litigationid: this.form.litigationid,
        })
      },
      /**
       * @description: 执行管理新增
       * @return {*}
       */
      zxglAdd() {
        if (!this.form.litigationid) {
          this.$message({
            type: 'error',
            message: '请先保存表单',
          })
          return
        }
        this.$refs['zxglModuleref'].show({
          litigationid: this.form.litigationid,
        })
      },
      /**
       * @description: 结案总结新增
       * @return {*}
       */
      jazjAdd() {
        if (!this.form.litigationid) {
          this.$message({
            type: 'error',
            message: '请先保存表单',
          })
          return
        }
        this.$refs['jazjAddref'].show({
          litigationid: this.form.litigationid,
          arbitraid: this.form.arbitraid,
        })
      },
      handleSelected(e) {
        this.$set(this.form, 'disputeunderName', e.realname)
        this.$set(this.form, 'disputeunder', e.staffid)
      },
      showDisputeItem(e) {
        this.$refs['disputeRef'].showEdit(this.form, true)
      },
      hold() {
        this.$message.error('请先保存基本信息!')
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getEditList()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getEditList()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.getEditList()
      },
      showDetail(row) {
        this.showEdit(row, true)
      },
      async fetchData(row) {
        const res = await litigationSettlementDetail({
          litigationId: row.litigationid,
        })

        if (res && res.litigation) {
          this.$set(this, 'form', res.litigation)
          this.$set(this.form, 'litigationid', res.litigation.litigationid)
          this.$set(this.form, 'disputeid', res.litigation.disputeid)
          this.$set(this.form, 'disputeItem', res.litigation.disputeidname)
          this.$set(
            this.form,
            'disputeunderName',
            res.litigation.disputeundername
          )
          this.baseInfo = {
            disputeid: res.litigation.disputeid,
            disputename: res.litigation.disputeidname,
          }
          this.uploadList()
          this.getEditList()
          this.fetchLawerList()
        }
      },
      async showEdit(row, disabled) {
        this.show = 0
        if (!row) {
          this.listLoading = false
          this.title = '添加'
        } else {
          this.listLoading = true
          if (disabled) {
            this.title = '查看'
            this.show = 1
          } else {
            this.title = '编辑'
          }

          this.fetchData(row)
        }
        this.dialogFormVisible = true
      },
      //附件列表
      async uploadList() {
        // this.listLoading = true
        const { data } = await findAttacheMent({
          type: 3,
          bid: this.form.litigationid,
        })
        this.uploadlist = data
        this.$refs.upload.clearFiles()
        this.$refs.upload.uploadFiles.length = 0
        // this.listLoading = false
      },
      //附件删除
      handleDelete1(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleAttacheMent({ aid: row.attid, type: 3 })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.uploadList()
        })
      },
      // 下载数据
      // downloadData(row) {
      //   const fileName = row.attname
      //   const link = document.createElement('a')
      //   link.download = fileName
      //   link.href = this.baseApi + '/download?id=' + row.attid
      //   link.style.display = 'none'
      //   document.body.appendChild(link)
      //   link.click()
      //   document.body.removeChild(link)
      // },
      async downloadData(row) {
        const data = await downloads({ attId: row.attid })
        let filename = row.attname
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      /**
       * @description: 文件上传限制
       * @param {*} file
       * @return {*}
       */
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 200
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      handleRemove(file, fileList) {},
      onSuccess(response, file, fileList) {
        this.uploadList()
      },
      onError(err) {
        this.$message.error(JSON.parse(err.message).message)
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.list = []
        this.uploadlist = []
        this.dialogFormVisible = false
        this.$emit('fetch-data')
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const bCreate = !this.form.litigationid
            this.isattorney = Number(this.isattorney || '')
            this.islinkcontract = Number(this.islinkcontract || '')
            this.disputestatus = Number(this.disputestatus || '')
            this.iscriminalcases = Number(this.iscriminalcases || '')
            this.isrepayment = Number(this.isrepayment || '')
            this.ispreservation = Number(this.ispreservation || '')
            this.execstatus = Number(this.execstatus || '')
            this.isurgent = Number(this.isurgent || '')

            const func = bCreate
              ? litigationSettlementSave
              : litigationSettlementModify
            const msg = bCreate ? '新增成功' : '修改成功'
            func(this.form).then((res) => {
              if (res.code == 1) {
                if (bCreate) {
                  this.form.litigationid = res.data
                }
                this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              }
            })
            this.$emit('fetch-data')
            // this.close()
          }
        })
      },
      showEditAdd(row) {
        if (!this.form.litigationid) {
          this.$message.error('请先保存基本信息!')
          return
        }
        this.$refs['editadd'].showEdit({
          litigationId: this.form.litigationid,
          ...row,
        })
      },
      handleSsjd(val) {
        this.form.disputeid = val.disputeid
        this.form.disputeItem = ''
        this.$set(this.form, 'disputeid', val.disputeid)
        this.$set(this.form, 'disputeItem', val.disputeitem)
        this.$set(this.form, 'disputetype', val.disputetype)
        this.$set(this.form, 'disputeno', val.disputeno)
        this.$set(this.form, 'disputeundertaker', val.disputeundertaker)
        this.$set(this.form, 'subjectamount', val.litigationamount)

        this.$set(this.form, 'contractid', val.contractid)
        // this.$set(this.form, 'litigiousstatus', val.litigationpos)
        this.$set(this.form, 'disputeunder', val.disputeundertaker)
        this.$set(this.form, 'disputeunderName', val.disputeundertakername)
        let litigiousstatus =
          val.whethersued == 1
            ? '原告'
            : val.whethersued == 2
            ? '被告'
            : val.whethersued == 3
            ? '上诉人'
            : val.whethersued == 4
            ? '被上诉人'
            : val.whethersued == 5
            ? '第三人'
            : val.whethersued == 6
            ? '再审申请人'
            : '被申请人'
        this.$set(this.form, 'litigiousstatus', litigiousstatus)
        this.$set(
          this.form,
          'islinkcontract',
          val.contractchildren == '是' ? 1 : 0
        )
        this.baseInfo = {
          disputeid: val.disputeid,
          disputename: val.disputeitem,
        }
      },
      handleSelectGlzc(val) {
        this.$set(this.form, 'courtfirst', val.courtfirst)
        this.$set(this.form, 'arbitraid', val.arbitraid)
        this.$set(this.form, 'createstaff', val.createstaff)
      },
      handleHt(item) {
        this.$set(this.form, 'contractname', item.contractname)
        this.$set(this.form, 'contractno', item.contractno)
        this.$set(this.form, 'realname', item.realname)
        this.$set(this.form, 'contractId', item.contractId)
      },
      handleeditadd() {
        this.getEditList()
      },
      handleDetailSsgc(row) {
        this.$refs['editadd'].showEdit({
          litigationId: this.form.litigationid,
          type: 'detail',
          ...row,
        })
      },
      handleEditSsgc(row) {
        this.$refs['editadd'].showEdit({
          litigationId: this.form.litigationid,
          type: 'edit',
          ...row,
        })
      },
      handleDeleteSsgc(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await removeLegalProceedingsRecord({
            proceedId: row.proceedid,
          })
          this.$baseMessage(
            msg || '操作成功',
            'success',
            'vab-hey-message-success'
          )
          await this.getEditList()
        })
      },
      async getEditList() {
        this.listLoading = true
        this.queryForm.litigationId = this.form.litigationid
        const {
          date: { tlist, totalRecord },
        } = await proceedingsRecord(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      // 增加一个空行, 用于录入或显示第一行
      addLine() {
        if (!this.form.arbitraid) {
          return this.$message({
            type: 'error',
            message: '请先保存表单',
          })
        }
        this.$refs['lowerEdit'].showModal({
          arbitraid: this.form.arbitraid,
        })
      },
      // 删除指定行
      async deleteRow(id) {
        const res = await legalAttorneyDelete({ id })
        this.fetchLawerList()
      },
      async fetchLawerList() {
        if (!this.form.arbitraid) return
        const res = await legalAttorney({ arbitraid: this.form.arbitraid })
      },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
      async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url }) // iframe弹框预览形式
      },
    },
  }
</script>
<style scoped>
  .dialog_btn {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
