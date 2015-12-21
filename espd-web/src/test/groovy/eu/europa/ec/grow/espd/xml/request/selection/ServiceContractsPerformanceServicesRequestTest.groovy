package eu.europa.ec.grow.espd.xml.request.selection

import eu.europa.ec.grow.espd.xml.base.AbstractSelectionCriteriaFixture
import eu.europa.ec.grow.espd.domain.EspdDocument
import eu.europa.ec.grow.espd.domain.SelectionCriterion
/**
 * Created by ratoico on 12/9/15 at 1:48 PM.
 */
class ServiceContractsPerformanceServicesRequestTest extends AbstractSelectionCriteriaFixture {

    def "15. should contain the 'For service contracts: performance of services of the specified type' criterion"() {
        given:
        def espd = new EspdDocument(serviceContractsPerformanceServices: new SelectionCriterion(exists: true))

        when:
        def request = parseRequestXml(espd)
        def idx = 0

        then: "CriterionID element"
        request.Criterion.size() == 1
        checkCriterionId(request, idx, "5e506c16-26ab-4e32-bb78-b27f87dc0565")

        then: "CriterionTypeCode element"
        checkCriterionTypeCode(request, idx, "SELECTION.TECHNICAL_PROFESSIONAL_ABILITY")

        then: "CriterionName element"
        request.Criterion[idx].Name.text() == "For service contracts: performance of services of the specified type"

        then: "CriterionDescription element"
        request.Criterion[idx].Description.text() == "During the reference period, the economic operator has provided the following main services of the type specified. Contracting authorities may require up to three years and allow experience dating from more than three years."

        then: "CriterionLegislationReference element"
        checkLegislationReference(request, idx, "58(4)")

        then: "check all the sub groups"
        request.Criterion[idx].RequirementGroup.size() == 4

        then: "check description amount date recipients"
        checkDescriptionAmountDateRecipients1Group(request.Criterion[idx].RequirementGroup[0])
        checkDescriptionAmountDateRecipients2Group(request.Criterion[idx].RequirementGroup[1])
        checkDescriptionAmountDateRecipients3Group(request.Criterion[idx].RequirementGroup[2])

        then: "info available electronically sub group"
        checkInfoAvailableElectronicallyRequirementGroup(request.Criterion[idx].RequirementGroup[3])
    }

}