const state = () => ({
  admin: false,
  role: [],
  ability: [],
  contractid: '',
  contractid2: ''
})
const getters = {
  admin: (state) => state.admin,
  role: (state) => state.role,
  ability: (state) => state.ability,
  contractid: (state) => state.contractid,
  contractid2: (state) => state.contractid2,
}
const mutations = {
  setFull(state, admin) {
    state.admin = admin
  },
  setRole(state, role) {
    state.role = role
  },
  setAbility(state, ability) {
    state.ability = ability
  },
  contractidd(state, contractid) {
    state.contractid = contractid
  },
  contractidd2(state, contractid) {
    state.contractid2 = contractid
  }
}
const actions = {
  setFull({ commit }, admin) {
    commit('setFull', admin)
  },
  setRole({ commit }, role) {
    commit('setRole', role)
  },
  setAbility({ commit }, ability) {
    commit('setAbility', ability)
  },
}
export default { state, getters, mutations, actions }
