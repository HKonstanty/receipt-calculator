package com.example.receiptapp.receipt

//import org.junit.Assert.*
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class GraphTest {
    private lateinit var graph: Graph

    @Before
    fun setup() {
        graph = Graph()
    }

    @Test
    fun `new graph is empty`() {
        assertThat(graph.getEdges().size).isEqualTo(0)
    }

    @Test
    fun `add three edges`() {
        graph.addEdge(1,2,4.0)
        graph.addEdge(2,3,5.0)
        graph.addEdge(1,3,3.0)
        assertThat(graph.getEdges().size).isEqualTo(3)
    }

    @Test
    fun `add the same edges`() {
        graph.addEdge(1, 2, 1.0)
        graph.addEdge(3, 4, 2.0)
        graph.addEdge(4, 3, 2.0)
        graph.addEdge(4, 4, 2.0)
        graph.addEdge(4, 4, 2.0)
        assertThat(graph.getEdges().size).isEqualTo(3)
    }

    @Test
    fun `repair graph`() {
        graph.addEdge(1, 2, 4.0)
        graph.addEdge(2, 2, -4.0)
        var result = true
        for (edge in graph.getEdges())
            if (edge.value < 0)
                result = false
        assertThat(result).isFalse()
        graph.repairGraph()
        result = true
        for (edge in graph.getEdges())
            if (edge.value < 0)
                result = false
        assertThat(result).isTrue()
    }
}