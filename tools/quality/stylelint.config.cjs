/** @type {import('stylelint').Config} */
module.exports = {
  extends: ['stylelint-config-standard-scss', 'stylelint-config-recess-order'],
  reportDescriptionlessDisables: true,
  reportInvalidScopeDisables: true,
  reportNeedlessDisables: true,
  reportUnscopedDisables: true,
  rules: {
    'no-empty-source': null,
    'selector-class-pattern': null,
    'scss/at-rule-no-unknown': [
      true,
      {
        ignoreAtRules: ['custom-variant'],
      },
    ],
  },
};
