/**
 * @description 登录、获取用户信息、退出登录、清除token逻辑，不建议修改
 */
import { getUserInfoEntity, login, socialLogin, getTokenByTicket, getTokenByV5ticket } from '@/oapi/user'
import img from '@/assets/avatat/1.jpg'
import { title, tokenName } from '@/config'
import { resetRouter } from '@/router'
import { getToken, removeToken, setToken } from '@/utils/token'
import Vue from 'vue'
const state = () => ({
  token: getToken(),
  username: '游客',
  // avatar: 'https://i.gtimg.cn/club/item/face/img/2/15922_100.gif',
  avatar: img,
})
const getters = {
  token: (state) => state.token,
  username: (state) => state.username,
  avatar: (state) => state.avatar,
}
const mutations = {
  /**
   * @description 设置token
   * @param {*} state
   * @param {*} token
   */
  setToken(state, token) {
    state.token = token
    setToken(token)
  },
  /**
   * @description 设置用户名
   * @param {*} state
   * @param {*} username
   */
  setUsername(state, username) {
    state.username = username
  },
  /**
   * @description 设置头像
   * @param {*} state
   * @param {*} avatar
   */
  setAvatar(state, avatar) {
    state.avatar = avatar
  },
}
const actions = {
  /**
   * @description 登录拦截放行时，设置虚拟角色
   * @param {*} { commit, dispatch }
   */
  setVirtualRoles({ commit, dispatch }) {
    dispatch('acl/setFull', true, { root: true })
    commit('setAvatar', 'https://i.gtimg.cn/club/item/face/img/2/15922_100.gif')
    // commit('setAvatar', '@/assets/avatat/1.gif')
    commit('setUsername', 'admin(未开启登录拦截)')
  },
  /**
   * @description 登录
   * @param {*} { commit }
   * @param {*} userInfo
   */
  async login({ commit }, userInfo) {
    console.log('userInfo----------------')
    if (!userInfo) {
      commit('setToken', 'xxxxxxxxxxxxxxxx')
    } else {
      console.log(userInfo)
      const {
        data: { [tokenName]: token },
      } = await login(userInfo)
      if (token) {
        commit('setToken', token)
        const hour = new Date().getHours()
        const thisTime =
          hour < 8
            ? '早上好'
            : hour <= 11
              ? '上午好'
              : hour <= 13
                ? '中午好'
                : hour < 18
                  ? '下午好'
                  : '晚上好'
        Vue.prototype.$baseNotify(`欢迎登录${title}`, `${thisTime}！`)
      } else {
        const err = `登录接口异常，未正确返回${tokenName}...`
        Vue.prototype.$baseMessage(err, 'error', 'vab-hey-message-error')
        throw err
      }
    }
  },
  async tokenLogin({ commit }, token) {
    commit('setToken', token)
  },
  /**
   * @description 第三方登录
   * @param {*} {}
   * @param {*} tokenData
   */
  async socialLogin({ commit }, tokenData) {
    const {
      data: { [tokenName]: token },
    } = await socialLogin(tokenData)
    if (token) {
      commit('setToken', token)
      const hour = new Date().getHours()
      const thisTime =
        hour < 8
          ? '早上好'
          : hour <= 11
            ? '上午好'
            : hour <= 13
              ? '中午好'
              : hour < 18
                ? '下午好'
                : '晚上好'
      Vue.prototype.$baseNotify(`欢迎登录${title}`, `${thisTime}！`)
    } else {
      const err = `login核心接口异常，请检查返回JSON格式是否正确，是否正确返回${tokenName}...`
      Vue.prototype.$baseMessage(err, 'error', 'vab-hey-message-error')
      throw err
    }
  },
  /**
   * @description ticket登录
   * @param {*} {}
   * @param {*} ticket
   */
  async loginByTicket({ commit }, ticket) {
    if (ticket) {
      const { code, data } = await getTokenByTicket({ ticket })
      if (code === 1 && data && data.token) {
        commit('setToken', data.token)
      } else {
        const err = `获取token失败...`
        Vue.prototype.$baseMessage(err, 'error', 'vab-hey-message-error')
        throw err
      }
    }
  },
  /**
   * @description v5ticket登录
   * @param {*} {}
   * @param {*} ticket
   */
  async loginByV5Ticket({ commit }, v5ticket) {
    if (v5ticket) {
      const { code, data } = await getTokenByV5ticket({ v5ticket })
      if (code === 1 && data && data.token) {
        commit('setToken', data.token)
      } else {
        const err = `获取token失败...`
        Vue.prototype.$baseMessage(err, 'error', 'vab-hey-message-error')
        throw err
      }
    }
  },
  /**
   * @description 获取用户信息接口 这个接口非常非常重要，如果没有明确底层前逻辑禁止修改此方法，错误的修改可能造成整个框架无法正常使用
   * @param {*} { commit, dispatch, state }
   * @returns
   */
  async getUserInfo({ commit, dispatch }, data) {
    // 临时用户信息
    if (data === null) {
      commit('setUsername', '管理员')
      commit(
        'setAvatar',
        'https://i.gtimg.cn/club/item/face/img/2/15922_100.gif'
      )
      commit('routes/setRoutes', [], { root: true })
    } else {
      let result = await getUserInfoEntity({ token: data.token })
      if (result.code === 1) {
        localStorage.setItem('userInfo', JSON.stringify(result.data.userInfo))
      }
    }
  },
  /**
   * @description 退出登录
   * @param {*} { dispatch }
   */
  async logout({ dispatch }) {
    // await logout()
    await dispatch('resetAll')
  },
  /**
   * @description 重置token、roles、ability、router、tabsBar等
   * @param {*} { commit, dispatch }
   */
  async resetAll({ commit, dispatch }) {
    commit('routes/setRoutes', [], { root: true })
    await dispatch('setToken', '')
    // await dispatch('acl/setFull', false, { root: true })
    await dispatch('acl/setRole', [], { root: true })
    await dispatch('acl/setAbility', [], { root: true })
    // await dispatch('tabs/delAllVisitedRoutes', null, { root: true })
    await resetRouter()
    removeToken()
  },
  /**
   * @description 设置token
   * @param {*} { commit }
   * @param {*} token
   */
  setToken({ commit }, token) {
    commit('setToken', token)
  },
  /**
   * @description 设置头像
   * @param {*} { commit }
   * @param {*} avatar
   */
  setAvatar({ commit }, avatar) {
    commit('setAvatar', avatar)
  },
}
export default { state, getters, mutations, actions }
