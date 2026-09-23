<template>
  <div class="yearPicker" ref="yearPicker" :style="{ width: width + 'px' }">
      <div class="_inner labelText" :style="{ width: labelWidth + 'px' }">{{ labelText }}</div>
      <input class="_inner" ref="inputLeft" v-model="startShowYear" @focus="onFocus" type="text" @click="clickInput"
          name="yearInput" @input="checkStartInput($event)" placeholder="选择年份" />
      <span>{{ sp }}</span>
      <input class="_inner" ref="inputRight" v-model="endShowYear" @focus="onFocus" type="text" @click="clickInput"
          name="yearInput" @input="checkEndInput($event)" placeholder="选择年份" />
      <div class="_inner floatPanel" v-if="showPanel">
          <div class="_inner leftPanel">
              <div class="_inner panelHead">
                  <i class="_inner el-icon-d-arrow-left" @click="onClickLeft"></i>
                  {{ leftYearList[0] + "-" + leftYearList[9] }}
              </div>
              <div class="_inner panelContent">
                  <div v-for="item in leftYearList" :class="{
                      disabled: checkValidYear(item)!= 0,
                      oneSelected: item === startYear && oneSelected,
                      startSelected: item === startYear,
                      endSelected: item === endYear,
                      _inner: true,
                      betweenSelected: item > startYear && item < endYear
                  }" :key="item">
                      <a :class="{
                          cell: true,
                          _inner: true,
                          selected: item === startYear || item === endYear
                      }" @click="onClickItem(item)" @mouseover="onHoverItem(item)">
                          {{ item }}
                      </a>
                  </div>
              </div>
          </div>
          <div class="_inner rightPanel">
              <div class="_inner panelHead">
                  <i class="_inner el-icon-d-arrow-right" @click="onClickRight"></i>
                  {{ rightYearList[0] + "-" + rightYearList[9] }}
              </div>
              <div class="_inner panelContent">
                  <div :class="{
                      disabled: checkValidYear(item) != 0,
                      startSelected: item === startYear,
                      endSelected: item === endYear,
                      betweenSelected: item > startYear && item < endYear
                  }" v-for="item in rightYearList" :key="item">
                      <a :class="{
                          cell: true,
                          _inner: true,
                          selected: item === endYear || item === startYear
                      }" @click="onClickItem(item)" @mouseover="onHoverItem(item)">
                          {{ item }}
                      </a>
                  </div>
              </div>
          </div>
      </div>
  </div>
</template>

<script>
const SELECT_STATE = {
  unselect: 0,
  selecting: 1,
  selected: 2,
};
export default {
  name: "YearPicker",
  computed: {
      oneSelected() {
          return (
              this.curState === SELECT_STATE.selecting &&
              (this.startYear === this.endYear || this.endYear == null)
          );
      },
      startDate() {
          return this.startYear;
      },
      leftYearList() {
          return this.yearList.slice(0, 10);
      },
      rightYearList() {
          return this.yearList.slice(10, 20);
      }

  },
  props: {
      width: {
          default: 200,
      },
      labelWidth: {
          default: 80,
      },
      labelText: {
          default: "时间标签",
      },
      sp: {
          default: "至",
      },
      initYear: {
          default: null,
      },
  },
  data() {
      return {
          itemBg: {},
          startShowYear: null,
          endShowYear: null,
          yearList: [],
          showPanel: false,
          startYear: null,
          endYear: null,
          curYear: 0,
          curSelectedYear: 0,
          curState: SELECT_STATE.unselect,
      };
  },
  methods: {
      checkValidYear(iYear) {
          if (this.initYear) {
              if (iYear > this.initYear.endYear) {
                  return 1
              } else if (iYear < this.initYear.startYear) {
                  return -1
              }
          }
          return 0
      },
      checkStartInput(event) {
          if (isNaN(this.startShowYear)) {
              this.startShowYear = this.startYear;
          } else {
              this.startYear = this.startShowYear * 1;
          }
      },

      checkEndInput() {
          if (isNaN(this.endShowYear)) {
              this.endShowYear = this.endYear;
          } else {
              this.endYear = this.endShowYear * 1;
          }
      },
      changeYear() {
          if (this.startYear > this.endYear) {
              let tmp = this.endYear;
              this.endYear = this.startYear;
              this.startYear = tmp;

          }
          if (this.initYear) {
              this.startYear = Math.max(this.startYear, this.initYear.startYear)
              this.endYear = Math.min(this.endYear, this.initYear.endYear)
          }
          this.startShowYear = this.startYear;
          this.endShowYear = this.endYear;


          if (this.startYear && this.endYear) {
              this.$emit("updateTimeRange", {
                  startYear: this.startYear,
                  endYear: this.endYear + ""
              });
          } else {
              console.warn("WARN:年份不合法", this.startYear, this.endYear);
          }
      },
      onHoverItem(iYear) {
          if (this.checkValidYear(iYear) != 0) {
              return;
          }
          if (this.curState === SELECT_STATE.selecting) {
              let tmpStart = this.curSelectedYear;
              this.endYear = Math.max(tmpStart, iYear);
              this.startYear = Math.min(tmpStart, iYear);
          }
      },
      onClickItem(iYear) {
          if (this.checkValidYear(iYear) != 0) {
              return;
          }

          if (
              this.curState === SELECT_STATE.unselect ||
              this.curState === SELECT_STATE.selected
          ) {
              this.startYear = iYear;
              this.curSelectedYear = iYear;
              this.endYear = null;
              this.curState = SELECT_STATE.selecting;
          } else if (this.curState === SELECT_STATE.selecting) {
              this.endShowYear = this.endYear;
              this.startShowYear = this.startYear;
              this.curState = SELECT_STATE.selected;
              this.$emit("updateTimeRange", {
                  startYear: this.startYear,
                  endYear: this.endYear,
              });

              setTimeout(() => {
                  //为动画留的时间，可优化
                  this.showPanel = false;
              }, 300);
          }
      },
      onFocus() {
          this.$nextTick(() => {
              this.showPanel = true;
          });
      },
      clickInput(e) {
          e.stopPropagation();
          return false;
      },

      updateYearList() {
          let iStart = Math.floor(this.curYear / 10) * 10 - 10;
          iStart = iStart < 0 ? 0 : iStart;
          this.yearList = [];
          for (let index = 0; index < 20; index++) {
              this.yearList.push(iStart + index);
          }
      },

      checkValidYear(iYear) {
          if (this.initYear) {
              if (iYear > this.initYear.endYear) {
                  return 1
              } else if (iYear < this.initYear.startYear) {
                  return -1
              }
          }
          return 0
      },
      closePanel(e) {
          if (!this.showPanel) {
              return;
          }
          if (typeof e.target.className !== "string" || e.target.className === "") {
              this.$nextTick(() => {
                  this.changeYear();
                  this.showPanel = false;
              });
              return;
          }
          if (
              e.target.className.indexOf("_inner") === -1 ||
              (e.target.name === "yearInput" &&
                  e.target !== this.$refs.inputLeft &&
                  e.target !== this.$refs.inputRight)
          ) {
              this.$nextTick(() => {
                  this.changeYear();
                  this.showPanel = false;
              });
          }

          e.stopPropagation();
          return false;
      },
      onClickLeft() {
          this.curYear = this.curYear * 1 - 10;
          this.updateYearList();
      },
      onClickRight() {
          this.curYear = this.curYear * 1 + 10;
          this.updateYearList();
      },

      //------------------对外接口------------------------
      // //直接传时间戳
      // setYear(startYearStamp, endYearStamp) {
      //     if (!isNaN(startYearStamp) && !isNaN(endYearStamp)) {
      //         let startYear = moment(startYearStamp).format("yyyy");
      //         let endYear = moment(endYearStamp).format("yyyy");
      //         this.startYear = startYear * 1;
      //         this.endYear = endYear * 1;
      //         this.endShowYear = endYear * 1;
      //         this.startShowYear = startYear * 1;
      //     }
      // },
  },

  created() {
      this.curYear = new Date().getFullYear();
      this.updateYearList();
  },
  beforeUnmount() {
      document.removeEventListener("click", this.closePanel.bind(this));
  },

  mounted() {
      document.addEventListener("click", this.closePanel.bind(this));
  },
};
</script>
<style lang="scss" scoped>
.yearPicker {
  font-size: 14px;
  display: flex;
  position: relative;
  transition: all 0.3s;
  input {
      text-align: center;
  }

  input:first-child {
      text-align: right;
  }

  background-color: #fff;

  .labelText {
      text-align: center;
  }

  span {
      padding: 0 8px;
      height: 32px;
      line-height: 32px;
  }

  border: 1px solid #eff1f3;
  height: 34px;
  line-height: 34px;
  border-radius: 4px;
  padding: 0 28px 0 8px;
  box-sizing: border-box;

  .floatPanel {
      >div {
          width: 50%;
      }

      padding: 0 16px;
      position: absolute;
      display: flex;
      background-color: #fff;
      z-index: 2000;
      border-radius: 4px;
      width: 650px;
      height: 250px;
      top: 40px;
      left: -10px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

      .panelContent {
          display: flex;
          flex-wrap: wrap;
          width: 100%;
          height: calc(100% - 70px);

          .disabled {
              color: #ccc;
          }

          .oneSelected {
              border-top-right-radius: 24px;
              border-bottom-right-radius: 24px;
          }

          .startSelected {
              background-color: #f6f6f7;
              border-top-left-radius: 24px;
              border-bottom-left-radius: 24px;
          }

          .endSelected {
              background-color: #f6f6f7;
              border-top-right-radius: 24px;
              border-bottom-right-radius: 24px;
          }

          .betweenSelected {
              background-color: #f6f6f7;
          }

          >div {
              width: 75px;
              height: 48px;
              line-height: 48px;
              margin: 3px 0;
              // border-radius: 24px;
              text-align: center;

              a {
                  display: inline-block;
                  width: 60px;
                  height: 36px;
                  cursor: pointer;
                  line-height: 36px;
                  border-radius: 18px;
              }

              .selected {
                  background-color: #3e77fc;
                  color: #fff;
              }
          }
      }

      .panelHead {
          position: relative;
          height: 46px;
          line-height: 46px;
          text-align: center;

          i {
              position: absolute;
              cursor: pointer;

              &:hover {
                  color: #3e77fc;
              }
          }
      }

      .rightPanel {
          padding-left: 8px;
      }

      .leftPanel .panelHead i {
          left: 20px;
          top:15px;
      }

      .rightPanel .panelHead i {
          right: 20px;
          top:15px;
      }
  }

  .floatPanel::before {
      content: "";
      height: 100%;
      position: absolute;
      left: 50%;
      width: 1px;
      border-left: 1px solid #e4e4e4;
  }
}

input {
  width: 60px;
  border: none;
  height: 32px;
  line-height: 32px;
  box-sizing: border-box;
  background-color: transparent;
}

input:focus {
  outline: none;
  background-color: transparent;
}

.yearPicker:hover {
  border-color: #3e77fc;
}

.dateIcon {
  position: absolute;
  right: 16px;
  top: 9px;
  color: #adb2bc;
}
</style>

​