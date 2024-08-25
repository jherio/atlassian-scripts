/*
 * DisplayFieldBasedOnComponent.groovy
 * 
 * This script controls the visibility and requirement of subcomponent fields
 * based on the selected components in the main component field.
 */

import com.atlassian.jira.component.ComponentAccessor
import com.onresolve.jira.groovy.user.FormField
import com.atlassian.jira.bc.project.component.ProjectComponent

final String COMPONENT_A = "A"
final String COMPONENT_B = "B"
final String FIELD_COMPONENT_X = "Component X version"
final String FIELD_COMPONENT_B = "Component B Version"

def componentManager = ComponentAccessor.getProjectComponentManager()

FormField mainComponentField = getFieldById(fieldChanged)
FormField subComponentXField = getFieldByName(FIELD_COMPONENT_X)
FormField subComponentBField = getFieldByName(FIELD_COMPONENT_B)

def selectedComponentIds = mainComponentField.getFormValue() as List ?: []

boolean hasComponentA = false
boolean hasComponentB = false

selectedComponentIds.each { componentId ->
    ProjectComponent component = componentManager.find(Long.parseLong(componentId as String))
    switch (component?.name) {
        case COMPONENT_A:
            hasComponentA = true
            break
        case COMPONENT_B:
            hasComponentB = true
            break
    }
}

if (hasComponentA && hasComponentB) {
    subComponentXField.setHidden(false)
    subComponentBField.setHidden(false)
    subComponentXField.setRequired(true)
    subComponentBField.setRequired(true)
} else if (hasComponentA) {
    subComponentXField.setHidden(true)
    subComponentBField.setHidden(false)
    subComponentBField.setRequired(true)
} else if (hasComponentB) {
    subComponentXField.setHidden(false)
    subComponentBField.setHidden(true)
    subComponentXField.setRequired(true)
} else {
    subComponentXField.setHidden(true)
    subComponentBField.setHidden(true)
}