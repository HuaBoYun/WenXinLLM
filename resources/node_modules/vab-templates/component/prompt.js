const { notEmpty } = require('../utils.js')

module.exports = {
  description: '创建component',
  prompts: [
    {
      type: 'input',
      name: 'name',
      message: '请输入component名称(可用/分隔),勿与之前重复,然后点击回车',
      validate: notEmpty('name'),
    },
  ],
  actions: () => {
    const pathCaseName = '{{ pathCase name }}'
    return [
      {
        type: 'add',
        path: `src/components/${pathCaseName}/index.vue`,
        templateFile: 'node_modules/vab-templates/component/index.hbs',
      },
    ]
  },
}
