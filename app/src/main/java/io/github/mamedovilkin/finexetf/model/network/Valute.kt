package io.github.mamedovilkin.finexetf.model.network

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Valute", strict = false)
data class Valute @JvmOverloads constructor(
    @field: Element(data = true, required = false)
    var Value: String = "",
    @field: Element(data = true, required = false)
    var VunitRate: String = "",
)