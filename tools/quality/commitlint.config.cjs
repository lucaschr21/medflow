module.exports = {
  extends: ['@commitlint/config-conventional'],
  defaultIgnores: true,
  helpUrl: 'https://commitlint.js.org',
  ignores: [
    (message) => message.startsWith('WIP'),
    (message) => /^Merge (branch|pull request)/.test(message),
  ],
  rules: {
    'header-max-length': [2, 'always', 100],
    'body-max-line-length': [2, 'always', 100],
    'footer-max-line-length': [2, 'always', 100],
    'scope-case': [2, 'always', 'kebab-case'],
    'type-enum': [
      2,
      'always',
      [
        'build',
        'chore',
        'ci',
        'docs',
        'feat',
        'fix',
        'perf',
        'refactor',
        'revert',
        'style',
        'test',
      ],
    ],
    'subject-case': [2, 'never', ['sentence-case', 'start-case', 'pascal-case', 'upper-case']],
  },
};
