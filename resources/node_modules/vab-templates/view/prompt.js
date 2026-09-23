const { notEmpty } = require('../utils.js')
module.exports = {
  description: '创建view',
  prompts: [
    {
      type: 'input',
      name: 'name',
      message: '请输入view名称(可用/分隔),勿与之前重复,然后点击回车',
      validate: notEmpty('name'),
    },
  ],
  actions: () => {
    const pathCaseName = '{{ pathCase name }}'
    return [
      {
        type: 'add',
        path: `src/views/${pathCaseName}/index.vue`,
        templateFile: 'node_modules/vab-templates/view/index.hbs',
      },
    ]
  },
}
