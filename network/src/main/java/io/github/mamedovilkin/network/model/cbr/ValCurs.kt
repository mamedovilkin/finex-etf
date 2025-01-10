package io.github.mamedovilkin.network.model.cbr

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "ValCurs", strict = false)
data class ValCurs @JvmOverloads constructor(
    @field: ElementList(data = true, inline = true, required = false)
    var Valute: List<Valute> = mutableListOf(),
    @field: Attribute(name = "Date", required = false)
    var Date: String = ""
)
