using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;
using UnityEngine.UI;
public class NPCMove : MonoBehaviour
{
    private NavMeshAgent agent;
    public Transform[] points;
    private int destPoint = 0;
    private Animator animator;
    public GameObject UI;
    public Text UIText;
    public string UIst;
    private bool isPathfindingPaused = false;
    void Start()
    {
        agent = GetComponent<NavMeshAgent>();
        animator = GetComponent<Animator>();
        UIText.text = UIst;
        // 初始化NPC的第一个目标点
        GotoNextPoint();
    }

    void GotoNextPoint()
    {
        // 如果没有设置目标点，返回
        if (points.Length == 0)
        {
            return;
        }

        // 随机选择一个目标点
        destPoint = Random.Range(0, points.Length);
        agent.destination = points[destPoint].position;
    }

    void Update()
    {
        if (isPathfindingPaused == false)
        {
          
            // 如果接近目标点
            if (agent.remainingDistance < 0.5f)
            {
                // 选择下一个目标点
                GotoNextPoint();
            }
        }
   

    }
    private void OnTriggerEnter(Collider other)
    {
        if (other.tag == "Player")
        {
            UI.gameObject.SetActive(true);
            // 暂停寻路
            isPathfindingPaused = true;
            agent.enabled = false;
            animator.enabled = false;
            // 使NPC面向主角
            Vector3 direction = other.transform.position - transform.position;
            direction.y = 0;
            transform.rotation = Quaternion.LookRotation(direction);
        
    }
}
    private void OnTriggerExit(Collider other)
    {
        if (other.tag == "Player")
        {
            UI.gameObject.SetActive(false);
            // 恢复寻路
            isPathfindingPaused = false;
            agent.enabled = true;
            animator.enabled = true;
            // 重新选择目标点开始寻路
            GotoNextPoint();
        }
    }
}
