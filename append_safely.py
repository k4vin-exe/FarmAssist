import os

def append_and_dedupe(base_file, additions_file):
    with open(base_file, 'r', encoding='utf-8') as f:
        base_lines = f.readlines()
        
    with open(additions_file, 'r', encoding='utf-8') as f:
        add_lines = f.readlines()
        
    # Get all additions ignoring <resources> tags and fix escaping
    new_items = []
    for line in add_lines:
        if '<string name=' in line:
            # fix escaping error from python's repr if any. e.g. \\'
            line = line.replace("\\\\'", "\\'")
            new_items.append(line)
            
    # We strip the closing </resources> from base_lines 
    while base_lines and ('</resources>' in base_lines[-1] or base_lines[-1].strip() == ''):
        base_lines.pop()
        
    # Combine
    combined = base_lines + new_items
    
    # Deduplicate based on keys
    seen_keys = set()
    final_lines = []
    
    for line in combined:
        if '<string name="' in line:
            try:
                key = line.split('<string name="')[1].split('">')[0]
                if key in seen_keys:
                    continue
                seen_keys.add(key)
            except:
                pass
        final_lines.append(line)
        
    final_lines.append('</resources>\n')
    
    with open(base_file, 'w', encoding='utf-8') as f:
        f.writelines(final_lines)

append_and_dedupe('app/src/main/res/values/strings.xml', 'values_en_additions.xml')
append_and_dedupe('app/src/main/res/values-ta/strings.xml', 'values_ta_additions.xml')
print('Successfully localized and deduplicated XML strings avoiding UTF issues!')
