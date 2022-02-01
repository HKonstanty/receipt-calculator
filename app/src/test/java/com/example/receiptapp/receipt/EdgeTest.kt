package com.example.receiptapp.receipt

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.Assert.*


class EdgeTest {
    /**
     * two edges with the reversed begin and end are the same
     */
    @Test
    fun `two edges with the opposite direction`() {
        val edge = Edge(1, 2, 10.0)
        val edge2 = Edge(2, 1, 5.0)
        assertThat(edge.same(edge2)).isTrue()
    }

    /**
     * two edges with the same begin and end are the same
     */
    @Test
    fun `two edges with the same direction`() {
        val edge = Edge(1, 2, 10.0)
        val edge2 = Edge(1, 2, 5.0)
        assertThat(edge.same(edge2)).isTrue()
    }

    /**
     * two edges with different begin or end
     */
    @Test
    fun `two edges with different direction`() {
        val edge = Edge(1, 2, 10.0)
        val edge2 = Edge(2, 3, 5.0)
        assertThat(edge.same(edge2)).isFalse()
    }

    /**
     * concat two edges with different direction
     */
    @Test
    fun `concat two edges with different direction`() {
        val edge = Edge(1, 2, 10.0)
        val edge2 = Edge(2, 1, 3.0)
        edge.concat(edge2)
        assertThat(edge.value).isEqualTo(7.0)
    }

    /**
     * concat two edges with same direction
     */
    @Test
    fun `concat two edges with same direction`() {
        val edge = Edge(1, 2, 10.0)
        val edge2 = Edge(1, 2, 3.0)
        edge.concat(edge2)
        assertThat(edge.value).isEqualTo(13.0)
    }

    /**
     * revers edge changes begin with end and change value to the opposite
     */
    @Test
    fun `revers edge`() {
        val edge = Edge(4, 1, 10.0)
        val edge2 = Edge(4, 1, 10.0)
        assertThat(edge.value).isEqualTo(edge2.value)
        assertThat(edge.begin).isEqualTo(edge2.begin)
        assertThat(edge.end).isEqualTo(edge2.end)
        edge.reversEdge()
        assertThat(edge.value).isNotEqualTo(edge2.value)
        assertThat(edge.begin).isEqualTo(edge2.end)
        assertThat(edge.end).isEqualTo(edge2.begin)
    }
}