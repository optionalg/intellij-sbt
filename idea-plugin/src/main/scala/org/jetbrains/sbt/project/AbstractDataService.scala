package org.jetbrains.sbt
package project

import java.util
import com.intellij.openapi.externalSystem.service.project.manage.ProjectDataService
import com.intellij.openapi.project.Project
import com.intellij.openapi.externalSystem.model.{Key, DataNode}
import com.intellij.openapi.externalSystem.util.ExternalSystemApiUtil

/**
 * @author Pavel Fatin
 */
abstract class AbstractDataService[E, I](key: Key[E]) extends ProjectDataService[E, I] {
  def getTargetDataKey = key

  final def importData(toImport: util.Collection[DataNode[E]], project: Project, synchronous: Boolean) {
    AbstractDataService.invoke(synchronous) {
      doImportData(toImport, project)
    }
  }

  def doImportData(toImport: util.Collection[DataNode[E]], project: Project)

  final def removeData(toRemove: util.Collection[_ <: I], project: Project, synchronous: Boolean) {
    AbstractDataService.invoke(synchronous) {
      doRemoveData(toRemove, project)
    }
  }

  def doRemoveData(toRemove: util.Collection[_ <: I], project: Project)
}

object AbstractDataService {
  def invoke(synchronous: Boolean)(block: => Unit) {
    ExternalSystemApiUtil.executeProjectChangeAction(synchronous, new Runnable {
      def run() {
        block
      }
    })
  }
}
