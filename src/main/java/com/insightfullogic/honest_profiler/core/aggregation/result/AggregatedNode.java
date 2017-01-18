package com.insightfullogic.honest_profiler.core.aggregation.result;

import static java.lang.Math.max;

import java.util.ArrayList;
import java.util.List;

import com.insightfullogic.honest_profiler.core.profiles.lean.FrameInfo;
import com.insightfullogic.honest_profiler.core.profiles.lean.NumericInfo;

/**
 * Wrapper for {@link AggregatedEntry} which allows organizing them into a tree
 * structure.
 */
public class AggregatedNode<K> implements Keyed<K>
{
    private final AggregatedEntry<K> entry;
    private final List<AggregatedNode<K>> children;

    public <T extends Keyed<K>> AggregatedNode(Aggregation<K, T> aggregation)
    {
        this.entry = new AggregatedEntry<>(null, aggregation);
        this.children = new ArrayList<>();
    }

    public AggregatedNode(AggregatedEntry<K> entry)
    {
        this.entry = entry;
        this.children = new ArrayList<>();
    }

    public AggregatedEntry<K> getEntry()
    {
        return entry;
    }

    public List<AggregatedNode<K>> getChildren()
    {
        return children;
    }

    public <T extends Keyed<K>> Aggregation<K, T> getAggregation()
    {
        return entry.getAggregation();
    }

    public NumericInfo getReference()
    {
        return entry.getAggregation().getReferenceData();
    }

    @Override
    public K getKey()
    {
        return entry.getKey();
    }

    public NumericInfo getData()
    {
        return entry.getData();
    }

    public long getSelfTime()
    {
        return entry.getSelfTime();
    }

    public long getTotalTime()
    {
        return entry.getTotalTime();
    }

    public int getSelfCnt()
    {
        return entry.getSelfCnt();
    }

    public int getTotalCnt()
    {
        return entry.getTotalCnt();
    }

    public double getSelfTimePct()
    {
        return entry.getSelfTimePct();
    }

    public double getTotalTimePct()
    {
        return entry.getTotalTimePct();
    }

    public double getSelfCntPct()
    {
        return entry.getSelfCntPct();
    }

    public double getTotalCntPct()
    {
        return entry.getTotalCntPct();
    }

    public int getRefCnt()
    {
        return getReference().getTotalCnt();
    }

    // Calculate deepest stack depth in descendants. Return 0 if there are no
    // children.
    public int getDescendantDepth()
    {
        if (children.isEmpty())
        {
            return 0;
        }

        int depth = 0;
        for (AggregatedNode<K> child : children)
        {
            depth = max(depth, child.getDescendantDepth() + 1);
        }
        return depth;
    }

    public void add(K key, FrameInfo frame, NumericInfo data)
    {
        entry.setKey(key);
        entry.add(frame, data);
    }

    public void addChild(AggregatedEntry<K> entry)
    {
        children.add(new AggregatedNode<>(entry));
    }

    public AggregatedNode<K> combine(AggregatedNode<K> other)
    {
        entry.combine(other.entry);
        children.addAll(other.children);
        return this;
    }
}