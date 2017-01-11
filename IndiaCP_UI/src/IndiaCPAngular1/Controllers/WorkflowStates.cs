using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace IndiaCPAngular1.Controllers
{
    public class WorkflowStates
    {
        public List<WorkflowState> states { get; set; }
        public WorkflowStates()
        {
            states.AddRange(new List<WorkflowState>() {
                new WorkflowState() { },
                new WorkflowState() { },
                new WorkflowState() { },
                new WorkflowState() { },
                new WorkflowState() { },
                new WorkflowState() { },
                new WorkflowState() { },
                new WorkflowState() { },
                new WorkflowState() { },
                new WorkflowState() { },
            });
        }
    }

    public class WorkflowState
    {
        public string status { get; set; }
        public string nextAction { get; set; }
        public String nodeType { get; set; }
    }
}
