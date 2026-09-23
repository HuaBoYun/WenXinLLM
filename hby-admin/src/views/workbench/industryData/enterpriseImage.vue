<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="企业画像"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
    >
      <div class="parent animation" id="css3d">
        <div class="wrap">
          <div class="box">
            <div
              style="
                width: 400px;
                text-align: right;
                margin-top: 100px;
                line-height: 40px;
              "
            >
              <span
                style="
                  font-size: 30px;
                  font-weight: border;
                  margin-right: 15px;
                  text-align: right;
                "
              >
                {{ objData.name }}
              </span>
              <br />
              <span
                style="font-size: 26px; font-weight: border; margin-right: 15px"
              >
                企业画像编号:
              </span>
              <br />
              <span
                style="font-size: 26px; font-weight: border; margin-right: 15px"
              >
                {{ objData.id }}
              </span>
            </div>
            <div
              style="
                width: 400px;
                text-align: center;
                margin-top: 80px;
                font-size: 26px;
                font-weight: border;
                color: #c0c0c0;
              "
            >
              经营数据清晰
            </div>
          </div>
          <!--<div class="box">
				
			</div>
			<div  class="box">
				
			</div>-->
          <div class="box">
            <div
              style="
                font-size: 24px;
                text-align: left;
                text-indent: 1em;
                font-weight: border;
                margin-top: 10px;
                color: #c0c0c0;
              "
            >
              <span style="font-size: 48px">良好</span>
              企业
            </div>
            <div
              style="
                font-size: 24px;
                text-align: left;
                text-indent: 2em;
                font-weight: border;
                margin-top: 10px;
                color: #c0c0c0;
              "
            >
              商务信用评分
              <span style="font-size: 48px; color: red">
                {{ objData.qypfsum }}
              </span>
            </div>
            <div
              id="main"
              style="
                margin-top: -100px;
                width: 400px;
                height: 400px;
                -webkit-tap-highlight-color: transparent;
                user-select: none;
                position: relative;
              "
              _echarts_instance_="ec_1659864857728"
            >
              <div
                style="
                  position: relative;
                  overflow: hidden;
                  width: 400px;
                  height: 400px;
                  padding: 0px;
                  margin: 0px;
                  border-width: 0px;
                  cursor: default;
                "
              >
                <canvas
                  data-zr-dom-id="zr_0"
                  width="800"
                  height="800"
                  style="
                    position: absolute;
                    left: 0px;
                    top: 0px;
                    width: 400px;
                    height: 400px;
                    user-select: none;
                    -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
                    padding: 0px;
                    margin: 0px;
                    border-width: 0px;
                  "
                ></canvas>
              </div>
              <div></div>
            </div>
          </div>
          <div class="box">
            <div class="zuo">
              <ul>
                <li style="width: 150px; color: #c0c0c0">
                  <p
                    class="shili1"
                    style="width: 50%; font-size: 40px; line-height: 55px"
                  >
                    行业综合实力
                  </p>
                </li>
                <li>
                  <p>{{ objData.tz1 }}</p>
                </li>
                <li>
                  <p>{{ objData.tz2 }}</p>
                </li>
                <li>
                  <p>{{ objData.tz3 }}</p>
                </li>
                <li>
                  <p>{{ objData.tz4 }}</p>
                </li>
                <li>
                  <p>{{ objData.tz5 }}</p>
                </li>
              </ul>
            </div>
          </div>
          <!--<div class="box">
				
			</div>-->
        </div>
      </div>
    </el-dialog>
    <depart-ment-dialog ref="depart" @select="handleSelectDep" />
  </div>
</template>

<script>
  import { userSave, roleList, jobList } from '@/api/setting/auth'
  import DepartMentDialog from '@/views/setting/auth/components/DepartMentDialog'
  import { qyhxList } from '@/api/workbench/telescope'
  export default {
    name: 'UserEdit',
    components: { DepartMentDialog },
    data() {
      return {
        disabled: false,
        form: {
          staffid: undefined,
          username: '',
          realname: '',
          email: '',
          miblephone: '',
          fixedphone: '',
          check: '0',
          name: '0',
          jobid: '',
          roleIdStrs: '',
          fatherorgid: 1,
          status: 1,
          orgid: undefined,
          orgname: '',
          memo: '',
          address: '',
        },
        rules: {
          username: [
            { required: true, trigger: 'blur', message: '请输入用户名' },
          ],
          realname: [
            { required: true, trigger: 'blur', message: '请输入真实姓名' },
          ],
          email: [{ required: false, trigger: 'blur', message: '请输入email' }],
          orgname: [
            { required: true, trigger: 'blur', message: '请选择所属机构' },
          ],
        },
        list: [],
        jobs: [],
        roles: [],
        title: '',
        dialogFormVisible: false,
        options: [
          {
            value: 1,
            label: '否',
          },
          {
            value: 0,
            label: '是',
          },
        ],
        data: [],
        id: '',
        companyName: '',
        objData: {},
      }
    },
    created() {},
    methods: {
      handleDep() {
        this.$refs.depart.show()
      },
      handleSelectDep(data) {
        this.form.orgid = data.id
        this.form.orgname = data.label
      },
      showEdit(id, name) {
        this.id = id
        this.companyName = name
        this.dialogFormVisible = true
        this.fetchHx()
      },
      async fetchHx() {
        let data = await qyhxList({
          id: this.id,
          name: this.companyName,
          reportName: '企业画像',
        })
        this.objData = data.data
        console.dir(data.data)
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            let param = JSON.parse(JSON.stringify(this.form))
            param.roleIdStrs = param.roleIdStrs.join(',')
            const { msg } = await userSave(param)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      async menuRole() {
        const {
          data: { tlist },
        } = await roleList({
          pageNumber: 1,
          pageSize: 1000,
        })
        this.roles = tlist
      },
      async menuPosition() {
        const {
          pageInfo: { tlist },
        } = await jobList({
          pageNumber: 1,
          pageSize: 1000,
        })
        this.jobs = tlist
      },
    },
  }
</script>
<style scoped>
  body {
    /* background:url("../../../images/dbj.jpg") no-repeat;  */
  }
  .wrap {
    width: 400px;
    height: 400px;
    /*border: 1px solid blue;*/
    position: relative;
    margin: 150px auto;
    transform-style: preserve-3d;
    transform: rotateX(-45deg) rotateY(-45deg);
  }
  .wrap > .box {
    position: absolute;
    top: 50;
    left: 0;
    width: 100%;
    height: 100%;
    background: url('~@/assets/telescope_images/zft.jpeg');
    background-size: cover;
    color: #fff;
  }
  .wrap > .box:nth-child(1) {
    transform: rotateX(90deg) rotateY(0deg) translateZ(200px);
  }
  .wrap > .box:nth-child(2) {
    transform: rotateX(0deg) rotateY(90deg) translateZ(200px);
  }
  .wrap > .box:nth-child(3) {
    transform: rotateX(0deg) rotateY(0deg) translateZ(200px);
  }
  .wrap > .box:nth-child(4) {
    transform: rotateX(90deg) rotateY(90deg) translateZ(200px);
  }
  .wrap > .box:nth-child(5) {
    transform: translateZ(200px);
  }
  .wrap > .box:nth-child(6) {
    transform: translateZ(-200px);
  }
  .zuo {
    float: left;
    width: 80%;
  }
  .zuo ul {
    list-style-type: none;
    margin: 0;
    padding: 0;
    width: 400px;
  }
  .zuo ul li {
    float: right;
    width: 9%;
  }
  .zuo ul li p {
    margin: 20px auto;
    font-size: 26px;
    line-height: 45px;
    padding: -40px;
    text-align: left;
  }
  .shili {
    width: 100px;
    color: #c0c0c0;
  }
  .shili1 {
    line-height: 60px;
    float: right;
    width: 100%;
    font-size: 43px;
  }
</style>
