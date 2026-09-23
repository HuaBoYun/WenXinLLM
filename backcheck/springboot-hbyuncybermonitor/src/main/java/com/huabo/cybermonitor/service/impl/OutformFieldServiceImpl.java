package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.cybermonitor.entity.OutformField;
import com.huabo.cybermonitor.entity.Tree;
import com.huabo.cybermonitor.mapper.OutformFieldMapper;
import com.huabo.cybermonitor.service.IOutformFieldService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-15
 */
@Service
public class OutformFieldServiceImpl extends ServiceImpl<OutformFieldMapper, OutformField> implements IOutformFieldService {

    /**
     * 获得所有节点
     * @param nodeId
     * @return
     */
    @Override
    public List<Tree> getNodeAll(BigDecimal nodeId) {
        List<Tree> trees = new ArrayList<Tree>();
        List<Tree> children = new ArrayList<Tree>();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("fieldid",nodeId);
        OutformField toff =  this.baseMapper.selectOne(qw);;

        Set<OutformField> chil = toff.getTblOutformFields();
        children = getNoteTrees(chil);
        Tree tree = new Tree();
        tree.setChildren(children);
        tree.setName(toff.getFieldname());
        tree.setId(toff.getFieldid());
        BigDecimal fa = null;
        if(toff.getTblOutformFields() == null){
            fa = new BigDecimal("-1");
        }else{
            fa = toff.getTblOutformField().getFieldid();
        }
        tree.setpId(fa);
        tree.setOpen(true);
        tree.setIsParent(toff.getTblOutformFields().size() > 0 ? true : false);
        trees.add(tree);
        return trees;
    }

    /**
     * 获得树
     * @param chil
     * @return
     */
    private List<Tree> getNoteTrees(Set<OutformField> chil) {
        List<Tree> children = new ArrayList<Tree>();
        for (OutformField toff : chil) {
            Tree tree = new Tree();
            if (toff.getTblOutformFields().size() > 0) {
                List<Tree> children1 = new ArrayList<Tree>();
                children1 = getNoteTrees(toff.getTblOutformFields());
                tree.setChildren(children1);
            }
            tree.setName(toff.getFieldname());
            tree.setId(toff.getFieldid());
            tree.setpId(toff.getTblOutformField().getFieldid());
            tree.setOpen(false);
            tree.setIsParent(toff.getTblOutformFields().size() > 0 ? true : false);
            children.add(tree);
        }
        return children;
    }
}
