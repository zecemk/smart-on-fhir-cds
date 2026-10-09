package srdc.smartcds
import io.onfhir.cds.OnFhirCds
import io.onfhir.config.FSConfigReader
import io.onfhir.r4.config.FhirR4Configurator
import srdc.smartcds.cds.CdsServiceFactory
import srdc.smartcds.config.{LlmConfig, SmartCdsConfig}

object Boot extends App {
  // Initialize FHIR version for the in-memory search
  val fhirConfigurator = new FhirR4Configurator()

  val fileSystemConfigReader = new FSConfigReader(fhirVersion = "R4")

  val fhirServerConfig = fhirConfigurator.initializeServerPlatform(
    fileSystemConfigReader,
    Set(
      "io.onfhir.operation.ValidationOperationHandler",
      "io.onfhir.operation.MetaOperationHandler",
      "io.onfhir.operation.DocumentOperationHandler",
      "io.onfhir.operation.LastNObservationOperationHandler",
      "io.onfhir.operation.ExpandOperationHandler"
    )
  )
  SmartCdsConfig.fhirServerConfig = Some(fhirServerConfig)

  val onfhirCds = OnFhirCds.asStandaloneServer(CdsServiceFactory)
  // Import service definitions to be used internally
  //SmartCdsConfig.cdsServiceDefinitions = onfhirCds.getConfig().cdsServiceDefinitions
  onfhirCds.start()
}