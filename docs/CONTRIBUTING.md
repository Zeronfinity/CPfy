## Contributing

If you want to contribute to CPfy, your help is very welcome!

### Pull Request Process

- Create a personal fork of the project on Github.
- Clone the fork on your local machine. Your remote repo on Github is called `origin`.
- Add the original repository as a remote called `upstream`.
- If you created your fork a while ago be sure to pull upstream changes into your local repository.
- Create a new branch to work on! Branch from `develop` if it exists, else from `master`. (Currently, `develop` branch does not exist)
- Implement/fix your feature.
- Follow the code style of the project, including indentation.
- Write or adapt tests as needed.
- Push your branch to your fork on Github, the remote `origin`.
- From your fork open a pull request in the correct branch. Target the project's `develop` branch if there is one, else go for `master`! (Currently, `develop` branch does not exist)
- Once the pull request is approved and merged you can pull the changes from `upstream` to your local repo and delete your extra branch(es).

If this is your first time contributing and you are confused with the whole workflow, try checking out the [first-contributions](https://github.com/firstcontributions/first-contributions) project to get started!

### Contributing a new feature

Please first discuss the change you wish to make via issue.

## Contribution Best Practices

### Commits
* Write clear meaningful git commit messages (Do read [here](http://chris.beams.io/posts/git-commit/))
* Make sure your PR's description contains GitHub's special keyword references that automatically close the related issue when the PR is merged. (For more info click [here]( https://github.com/blog/1506-closing-issues-via-pull-requests))
* When you make very very minor changes to a PR of yours (like for example fixing a failing Travis build or some small style corrections or minor changes requested by reviewers), consider squashing your commits afterwards so that you don't have an absurd number of commits for a very small fix. (Learn how to squash at [here](https://davidwalsh.name/squash-commits-git ))
* When you're submitting a PR for a UI-related issue, it would be really awesome if you add a screenshot of your change or a link to a deployment where it can be tested out along with your PR. It makes it very easy for the reviewers and you'll also get reviews quicker.

### Feature Requests and Bug Reports
* When you file a feature request or when you are submitting a bug report to the [issue tracker](https://github.com/Zeronfinity/CPfy/issues), make sure you add steps to reproduce it. Especially if that bug is some weird/rare one.
