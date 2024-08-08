import * as types from './types'

export default {
  state: {
    moduleTree: null,
    selectModuleIds: null
  },
  mutations: {
    [types.SET_MODULETREE]: (state, data) => {
      state.moduleTree = data
    },
    [types.SET_SELECT_MODULES]: (state, data) => {
      state.selectModuleIds = data
    }
  },
  getters: {
    getFromData: (state, getters) => {
      if (!state.moduleTree) {
        return []
      }
      if (!state.selectModuleIds) {
        return state.moduleTree
      }
      let userData = state.selectModuleIds.split(',')
      return JSON.parse(JSON.stringify(state.moduleTree)).filter(item1 => {
        if (item1.children && item1.children.length > 0) {
          item1.children = item1.children.filter(item2 => {
            if (item2.children && item2.children.length > 0) {
              item2.children = item2.children.filter(item3 => {
                return userData.every(selectid => {
                  return selectid !== item3.id
                })
              })
              return item2.children.length > 0
            }
            return userData.every(selectid => {
              return selectid !== item2.id
            })
          })
          return item1.children.length > 0
        }
        return userData.every(selectid => {
          return selectid !== item1.id
        })
      })
    },
    getToData: (state, getters) => {
      if (!state.moduleTree || !state.selectModuleIds) {
        return []
      }
      let userData = state.selectModuleIds.split(',')
      return JSON.parse(JSON.stringify(state.moduleTree)).filter(item1 => {
        if (item1.children && item1.children.length > 0) {
          item1.children = item1.children.filter(item2 => {
            if (item2.children && item2.children.length > 0) {
              item2.children = item2.children.filter(item3 => {
                return userData.some(selectid => {
                  return selectid === item3.id
                })
              })
              return item2.children.length > 0
            }
            return userData.some(selectid => {
              return selectid === item2.id
            })
          })
          return item1.children.length > 0
        }
        return userData.some(selectid => {
          return selectid === item1.id
        })
      })
    }
  }
}
