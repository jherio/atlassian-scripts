# Atlassian Scripts

This is a small collection of scripts used within Jira, Confluence, and Insight. These scripts are designed to automate various tasks and enhance functionality in Atlassian products.

## Contents

1. Jira Scripts
   - REST Endpoints
   - Validators
   - Post Functions
   - ScriptRunner Scripts
   - Behaviours

2. Confluence Scripts
   - ScriptRunner Scripts

3. Insight Scripts
   - Post Functions
   - Validators

## Jira Scripts

### REST Endpoints
- CreateIssueFromJenkins.groovy: A script to create Jira issues from Jenkins.
- ExampleRestEndpoint.groovy: Demonstrates receiving XML data, processing it, and returning a JSON response.

### Validators
- UserIsProjectAdmin.groovy: Validates if a user is a project admin or lead.

### Post Functions
- StartJenkinsJob.groovy: Triggers a Jenkins job from a Jira post function based on custom field values.

### ScriptRunner Scripts
- BulkRemoveProjects.groovy: Bulk removes Jira projects based on a CSV input file.

### Behaviours
- CustomErrorEmptyField.groovy: Validates that the "Fix Version/s" field is not empty and displays an error message if it is.
- DisplayFieldBasedOnComponent.groovy: Controls the visibility and requirement of subcomponent fields based on the selected components in the main component field.

## Confluence Scripts

### ScriptRunner Scripts
- PurgePageVersionHistory.groovy: Purges old versions of Confluence pages, keeping a specified number of recent versions.

## Insight Scripts

### Post Functions
- UserCreateJSD.groovy: Creates a Jira user based on Insight object data.
- UserUpdateJSD.groovy: Updates a Jira user based on Insight object data.

### Validators
- ValidateInsightCustomField.groovy: Validates an Insight custom field value.

## Usage

Each script is designed for a specific purpose within Jira, Confluence, or Insight. Please refer to the individual script files for detailed usage instructions and any required setup. Many scripts include comments at the top explaining their purpose and how to use them.

## Contributing

Feel free to contribute to this collection by adding new scripts or improving existing ones. Please ensure that any contributions are well-documented and follow best practices for Atlassian scripting.

## License

This project is open-source and available under the [MIT License](LICENSE).