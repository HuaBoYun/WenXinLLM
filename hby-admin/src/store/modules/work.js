const state = () => ({
  processMobile: false,

  wddbDetails: {},
  wddbState: false,

  wdcyDetails: {},
  wdcyState: false,

  wfqdDetails: {},
  wfqdState: false,

  cssyDetails: {},
  cssyState: false,
})

const getters = {
  workDetails: (state) => state.wddbDetails,
  wddbState: (state) => state.wddbState,

  workDetails1: (state) => state.wdcyDetails,
  wdcyState: (state) => state.wdcyState,

  workDetails2: (state) => state.wfqdDetails,
  wfqdState: (state) => state.wfqdState,

  workDetails3: (state) => state.cssyDetails,
  cssyState: (state) => state.cssyState,
}

const mutations = {
  setProcessMobile(state, data) {
    state.processMobile = data
  },
  setWbbdDetails(state, data) {
    state.wddbDetails = data
  },
  setWddbState(state, data) {
    state.wddbState = data
  },

  setWbcyDetails(state, data) {
    state.wdcyDetails = data
  },
  setWdcyState(state, data) {
    state.wdcyState = data
  },

  setWfqdDetails(state, data) {
    state.wfqdDetails = data
  },
  setWfqdState(state, data) {
    state.wfqdState = data
  },

  setCssyDetails(state, data) {
    state.cssyDetails = data
  },
  setCssyState(state, data) {
    state.cssyState = data
  },
}

const actions = {
  setProcessMobileAction({ commit }, data) {
    commit('setProcessMobile', data)
  },
  setWbbdDetailsAction({ commit }, data) {
    commit('setWbbdDetails', data)
  },
  setWddbStatesAction({ commit }, data) {
    commit('setWddbState', data)
  },

  setWbcyDetailsAction({ commit }, data) {
    commit('setWbcyDetails', data)
  },
  setWdcyStatesAction({ commit }, data) {
    commit('setWdcyState', data)
  },

  setWfqdDetailsAction({ commit }, data) {
    commit('setWfqdDetails', data)
  },
  setWfqdStatesAction({ commit }, data) {
    commit('setWfqdState', data)
  },

  setCssyDetailsAction({ commit }, data) {
    commit('setCssyDetails', data)
  },
  setCssyStatesAction({ commit }, data) {
    commit('setCssyState', data)
  },
}

export default {
  state,
  getters,
  mutations,
  actions,
}
